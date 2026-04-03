package com.app.rupiksha.models

import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root

@Root(name = "CustOpts")
data class CustOpts(
    @field:ElementList(name = "Param", required = false, inline = true)
    var params: List<Param>? = null
)
