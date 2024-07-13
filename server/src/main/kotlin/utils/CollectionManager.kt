package utils

import kotlinx.serialization.Serializable
import commandArgumentsAndTheirsComponents.Route
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.concurrent.LinkedBlockingDeque
import java.util.concurrent.LinkedBlockingQueue

/***
 * Класс, работающий с коллекцией
 * @author Demid0
 * @since 1.0
 * @param collection
 * Поле, в котором хранится коллекция
 * @param supportedCollectionTypes
 * Поддерживаемые типы коллекции
 * @param fileName
 * Имя файла, в который сохраняется коллекция
 * @param infoFileName
 * Имя файла, в который сохраняется информация о коллекции
 */
class CollectionManager: KoinComponent {
    @Serializable
    var collection: MutableCollection<Route> = LinkedBlockingDeque()
    private var supportedCollectionTypes: HashMap<String, MutableCollection<Route>> = hashMapOf()
    private val dbHandler: DBHandler by inject()

    init {
        addSupportedCollectionType("linkedblockingqueue", LinkedBlockingQueue())
        addSupportedCollectionType("linkedblockingdeque", LinkedBlockingDeque())

        val type = supportedCollectionTypes.filter { it.value::class == collection::class }.keys.first()
        collection = dbHandler.downloadCurrentCollection()
        changeType(type)
    }
    fun changeType(newType: String) {
        if (collection == getSupportedCollectionTypes()[newType]!!) return
        val old = collection
        collection = supportedCollectionTypes[newType.lowercase()]!!
        for (element in old) {
            collection.add(element)
        }
    }
    fun addSupportedCollectionType(name: String, collection: MutableCollection<Route>) {
        supportedCollectionTypes[name] = collection
    }
    fun getSupportedCollectionTypes() = supportedCollectionTypes

}