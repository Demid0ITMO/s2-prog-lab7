package builders

import commandArgumentsAndTheirsComponents.MyString

class MyStringBuilder {
    var it: String? = null
    fun build() = MyString(it!!)
}
