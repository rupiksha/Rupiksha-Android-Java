package com.app.rupiksha.models

import org.simpleframework.xml.Attribute
import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

@Root(name = "DeviceInfo", strict = false)
data class DeviceInfo(
    @field:Attribute(name = "dpId", required = false)
    var dpld: String? = null,

    @field:Attribute(name = "rdsId", required = false)
    var rdsld: String? = null,

    @field:Attribute(name = "rdsVer", required = false)
    var rdsVer: String? = null,

    @field:Attribute(name = "dc", required = false)
    var dc: String? = null,

    @field:Attribute(name = "mi", required = false)
    var mi: String? = null,

    @field:Attribute(name = "mc", required = false)
    var mc: String? = null,

    @field:Element(name = "additional_info", required = false)
    var add_info: AdditionalInfo? = null
)
