package com.app.rupiksha.models

import org.simpleframework.xml.Attribute

data class Resp(
    @field:Attribute(name = "errCode", required = false)
    var errCode: String? = null,

    @field:Attribute(name = "errInfo", required = false)
    var errInfo: String? = null,

    @field:Attribute(name = "fCount", required = false)
    var fCount: String? = null,

    @field:Attribute(name = "fType", required = false)
    var fType: String? = null,

    @field:Attribute(name = "iCount", required = false)
    var iCount: String? = null,

    @field:Attribute(name = "iType", required = false)
    var iType: String? = null,

    @field:Attribute(name = "pCount", required = false)
    var pCount: String? = null,

    @field:Attribute(name = "pType", required = false)
    var pType: String? = null,

    @field:Attribute(name = "nmPoints", required = false)
    var nmPoints: String? = null,

    @field:Attribute(name = "qScore", required = false)
    var qScore: String? = null
)
