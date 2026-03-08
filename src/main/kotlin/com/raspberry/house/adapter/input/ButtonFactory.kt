package com.raspberry.house.adapter.input

import com.diozero.api.DebouncedDigitalInputDevice
import com.diozero.api.GpioPullUpDown

object ButtonFactory {

    fun createButton(pin: Int): DebouncedDigitalInputDevice {
        return DebouncedDigitalInputDevice(
            pin,
            GpioPullUpDown.NONE,
            50)
    }

}
