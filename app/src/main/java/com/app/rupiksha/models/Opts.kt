package com.app.rupiksha.models

import org.simpleframework.xml.Attribute
import org.simpleframework.xml.Root

@Root(name = "Opts")
data class Opts(
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

    @field:Attribute(name = "format", required = false)
    var format: String? = null,

    @field:Attribute(name = "pidVer", required = false)
    var pidVer: String? = null,

    @field:Attribute(name = "timeout", required = false)
    var timeout: String? = null,

    @field:Attribute(name = "otp", required = false)
    var otp: String? = null,

    @field:Attribute(name = "wadh", required = false)
    var wadh: String? = null,

    @field:Attribute(name = "nmPoints", required = false)
    var nmPoints: String? = null,

    @field:Attribute(name = "posh", required = false)
    var posh: String? = null,

    @field:Attribute(name = "env", required = false)
    var env: String? = null // S == Stage, P == Prod, PP == PreProd
)
