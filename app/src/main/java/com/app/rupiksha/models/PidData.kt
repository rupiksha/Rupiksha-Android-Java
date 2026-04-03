package com.app.rupiksha.models

import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

@Root(name = "PidData")
data class PidData(
    @field:Element(name = "Resp", required = false)
    var _Resp: Resp? = null,

    @field:Element(name = "DeviceInfo", required = false)
    var _DeviceInfo: DeviceInfo? = null,

    @field:Element(name = "Skey", required = false)
    var _Skey: Skey? = null,

    @field:Element(name = "Hmac", required = false)
    var _Hmac: String? = null,

    @field:Element(name = "Data", required = false)
    var _Data: Data? = null
)
