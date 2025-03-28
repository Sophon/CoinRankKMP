package org.example.udemykmp.core.util

import org.example.udemykmp.core.domain.DataError
import org.jetbrains.compose.resources.StringResource
import udemykmp.composeapp.generated.resources.Res
import udemykmp.composeapp.generated.resources.error_disk_full
import udemykmp.composeapp.generated.resources.error_insufficient_balance
import udemykmp.composeapp.generated.resources.error_no_internet
import udemykmp.composeapp.generated.resources.error_request_timeout
import udemykmp.composeapp.generated.resources.error_serialization
import udemykmp.composeapp.generated.resources.error_too_many_requests
import udemykmp.composeapp.generated.resources.error_unknown

fun DataError.toUiText(): StringResource {
    return when (this) {
        DataError.Local.DISK_FULL -> Res.string.error_disk_full
        DataError.Local.INSUFFICIENT_FUNDS -> Res.string.error_insufficient_balance
        DataError.Remote.SERIALIZATION_ERROR -> Res.string.error_serialization
        DataError.Remote.REQUEST_TIMEOUT -> Res.string.error_request_timeout
        DataError.Remote.TOO_MANY_REQUESTS -> Res.string.error_too_many_requests
        DataError.Remote.NO_INTERNET -> Res.string.error_no_internet

        DataError.Local.UNKNOWN,
        DataError.Remote.SERVER_ERROR,
        DataError.Remote.UNKNOWN -> Res.string.error_unknown
    }
}