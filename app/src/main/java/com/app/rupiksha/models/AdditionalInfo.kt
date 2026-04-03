package com.app.rupiksha.models

import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root

@Root(name = "additional_info", strict = false)
data class AdditionalInfo(
    @field:ElementList(name = "Param", required = false, inline = true)
    var params: List<Param>? = null
)
