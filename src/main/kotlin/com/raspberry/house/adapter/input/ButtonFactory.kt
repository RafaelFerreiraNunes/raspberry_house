package com.raspberry.house.adapter.input

import com.diozero.api.DigitalInputDevice
import com.diozero.api.GpioEventTrigger
import com.diozero.api.GpioPullUpDown

object ButtonFactory {

    fun createButton(pin: Int): DigitalInputDevice {
        return DigitalInputDevice(
            pin,
            GpioPullUpDown.NONE,
            GpioEventTrigger.BOTH)
    }

}
