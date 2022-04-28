package ir.ali.weatherforecast.utils


/**
 Use this Interface to detect when dialog appears on our Activity
 * */
interface DialogAppear {
    fun onDialogAppeared(ip: String?, port: Int?, status:Boolean?, type: String? )
}