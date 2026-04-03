package com.app.rupiksha.models

import org.simpleframework.xml.Attribute
import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

@Root(name = "PidOptions", strict = false)
data class PidOptions(
    @field:Attribute(name = "ver", required = false)
    var ver: String? = null,

    @field:Element(name = "Opts", required = false)
    var Opts: Opts? = null,

    @field:Element(name = "CustOpts", required = false)
    var CustOpts: CustOpts? = null
)
