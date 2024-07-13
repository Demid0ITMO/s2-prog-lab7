package utils

import builders.route
import commandArgumentsAndTheirsComponents.Coordinates
import commandArgumentsAndTheirsComponents.Location
import commandArgumentsAndTheirsComponents.Route
import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet
import java.sql.SQLException
import java.util.concurrent.LinkedBlockingDeque
import java.util.logging.Level
import java.util.logging.Logger

class DBHandler(
    url: String,
    user: String,
    password: String) {
    private val connection: Connection = DriverManager.getConnection(url, user, password)
    private val statement = connection.createStatement()
    private val logger = Logger.getLogger("DB Logger")

    fun checkUser(username: String, password: String): Long {
        return try {
            val query = "select * from users where username='$username'"
            val res = executeQuery(query)
            val pass = res.getString("password")
            if (pass != password) -1
            else res.getLong("id")
        } catch (e: SQLException) {
            logger.log(Level.WARNING, e.message)
            -1
        }
    }

    fun setUser(username: String, password: String): Long {
        return try {
            val query = "insert into users (username, password) values ('$username', '$password') returning id"
            val res = executeQuery(query)
            res.getLong("id")
        } catch (e: SQLException) {
            logger.log(Level.WARNING, e.message)
            -1
        }
    }

    fun addElement(element: Route, user_id: Long): Long {
        return try {
            var query = coordinatesInsertQuery(element.getCoordinates())
            var res = executeQuery(query)
            val coordinates_id = res.getLong("id")
            val from = element.getFrom()
            var from_id: Long? = null
            if (from != null) {
                query = locationInsertQuery(from)
                res = executeQuery(query)
                from_id = res.getLong("id")
            }
            val to = element.getTo()
            query = locationInsertQuery(to)
            res = executeQuery(query)
            val to_id = res.getLong("id")
            query = "insert into collection_elements (creation_date, user_id, " +
                    "name, coordinates_id, from_id, to_id, distance) values " +
                    "(TO_TIMESTAMP('${element.getCreationDate()}','dd-mm-yyyy HH24:MI:ss'), " +
                    "$user_id, '${element.getName()}', $coordinates_id, $from_id, $to_id, ${element.getDistance()}) returning id"
            res = executeQuery(query)
            val ans = res.getLong("id")
            query = "update coordinates set route_id = $ans where id=$coordinates_id"
            executeUpdate(query)
            query = "update location set route_id = $ans where id in ($to_id, $from_id)"
            executeUpdate(query)
            ans
        } catch (e: SQLException) {
            logger.log(Level.WARNING, e.message)
            -1
        }
    }

    fun removeElement(id: Long, user_id: Long): Boolean {
        return try {
            var query = "select user_id from collection_elements where id=$id"
            val res = executeQuery(query)
            if (res.getLong("user_id") != user_id) false
            else {
                query = "delete from collection_elements where id=$id"
                executeUpdate(query)
                true
            }
        } catch (e: SQLException) {
            logger.log(Level.WARNING, e.message)
            false
        }
    }

    fun clearCollection(user_id: Long): Boolean {
        return try {
            val query = "delete from collection_elements where user_id=$user_id"
            executeUpdate(query)
            true
        } catch (e: SQLException) {
            logger.log(Level.WARNING, e.message)
            false
        }
    }

    fun updateElement(element: Route, user_id: Long): Boolean {
        return try {
            var query = "select * from collection_elements where id=${element.getId()}"
            val res = executeQuery(query)
            if (res.getLong("user_id") != user_id) false
            else {
                query = "update coordinates set " +
                        "x=${element.getCoordinates().getX()}, " +
                        "y=${element.getCoordinates().getY()} " +
                        "where id=${res.getLong("coordinates_id")};\n"
                query += "update location set " +
                        "x=${element.getTo().getX()}, " +
                        "y=${element.getTo().getY()}, " +
                        "z=${element.getTo().getZ()}, " +
                        "name=${element.getTo().getName()} " +
                        "where id=${res.getLong("to_id")};\n"
                val from = element.getFrom()
                query += if (from != null) {
                    "update location set " +
                    "x=${from.getX()}, " +
                    "y=${from.getY()}, " +
                    "z=${from.getZ()}, " +
                    "name=${from.getName()} " +
                    "where id=${res.getLong("from_id")};"
                } else {
                    "delete from location where id=${res.getLong("from_id")};"
                }
                executeUpdate(query)
                query = "update collection_elements set " +
                        "name=${element.getName()}, " +
                        "distance=${element.getDistance()}, " +
                        "from_id=${if (from == null) null else res.getLong("from_id")} " +
                        "where id=${element.getId()};"
                executeUpdate(query)
                true
            }
        } catch (e: SQLException) {
            logger.log(Level.WARNING, e.message)
            false
        }
    }
    private fun executeUpdate(s: String) { statement.executeUpdate(s) }
    private fun executeQuery(s: String, withNext: Boolean = true): ResultSet {
        val res = statement.executeQuery(s)
        if (withNext) res.next()
        return res
    }
    private fun coordinatesInsertQuery(coordinates: Coordinates) = "insert into coordinates (x, y) values (${coordinates.getX()}, ${coordinates.getY()}) returning id"
    private fun locationInsertQuery(location: Location) = "insert into location (x, y, z, name) values (${location.getX()}, ${location.getY()}, ${location.getZ()}, '${location.getName()}') returning id"
    fun downloadCurrentCollection(): LinkedBlockingDeque<Route> {
        var query = "select * from collection_elements"
        val res = connection.createStatement().executeQuery(query)
        val ans = LinkedBlockingDeque<Route>()
        while (res.next()) {
            val user_id = res.getLong("user_id")
            val from_id = if (res.getString("from_id") == null) null else res.getString("from_id")
            val to_id = res.getString("to_id")
            val coordinates_id = res.getString("coordinates_id")
            var res1: ResultSet
            ans.add(
                route {
                    id = res.getLong("id")
                    creationDate = res.getDate("creation_date")
                    name = res.getString("name")
                    distance = res.getDouble("distance")
                    coordinates = coordinates {
                        query = "select * from coordinates where id=$coordinates_id"
                        res1 = executeQuery(query)
                        x = res1.getFloat("x")
                        y = res1.getInt("y")
                    }
                    from = if (from_id == null) null else location {
                        query = "select * from location where id=$from_id"
                        res1 = executeQuery(query)
                        x = res1.getInt("x")
                        y = res1.getFloat("y")
                        z = res1.getLong("z")
                        name = res1.getString("name")
                    }
                    to = location {
                        query = "select * from location where id=$to_id"
                        res1 = executeQuery(query)
                        x = res1.getInt("x")
                        y = res1.getFloat("y")
                        z = res1.getLong("z")
                        name = res1.getString("name")
                    }
                    setOwner(user_id)
                }
            )
        }
        return ans
    }
}