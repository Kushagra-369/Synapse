package com.synapse.mobile.core.container

import android.content.Context
import com.synapse.mobile.core.dispatcher.SkillRegistry
import com.synapse.mobile.features.skills.clock.ClockSkill
import com.synapse.mobile.features.skills.clock.gateway.AndroidClockGateway
import com.synapse.mobile.features.skills.clock.gateway.ClockGateway
import com.synapse.mobile.features.skills.timer.TimerSkill
import com.synapse.mobile.features.skills.timer.gateway.AndroidTimerGateway
import com.synapse.mobile.features.skills.timer.gateway.TimerGateway
import com.synapse.mobile.features.skills.stopwatch.StopwatchSkill
import com.synapse.mobile.features.skills.stopwatch.gateway.AndroidStopwatchGateway
import com.synapse.mobile.features.skills.stopwatch.gateway.StopwatchGateway
import com.synapse.mobile.features.skills.calendar.CalendarSkill
import com.synapse.mobile.features.skills.calendar.gateway.AndroidCalendarGateway
import com.synapse.mobile.features.skills.calendar.gateway.CalendarGateway
import com.synapse.mobile.features.skills.phone.PhoneSkill
import com.synapse.mobile.features.skills.phone.gateway.AndroidPhoneGateway
import com.synapse.mobile.features.skills.phone.gateway.PhoneGateway
import com.synapse.mobile.features.skills.contacts.ContactSkill
import com.synapse.mobile.features.skills.contacts.gateway.AndroidContactGateway
import com.synapse.mobile.features.skills.contacts.gateway.ContactGateway
import com.synapse.mobile.features.skills.apps.AppsSkill
import com.synapse.mobile.features.skills.apps.gateway.AndroidAppGateway
import com.synapse.mobile.features.skills.apps.gateway.AppGateway
class AppContainer(
    context: Context
) {

    val clockGateway: ClockGateway =
        AndroidClockGateway(context)

    val timerGateway: TimerGateway =
        AndroidTimerGateway(context)

    val stopwatchGateway: StopwatchGateway =
        AndroidStopwatchGateway(context)

    val calendarGateway: CalendarGateway =
        AndroidCalendarGateway(context)

    val contactGateway: ContactGateway =
        AndroidContactGateway(context.contentResolver)

    val appGateway: AppGateway =
        AndroidAppGateway(context)

    val phoneGateway: PhoneGateway =
        AndroidPhoneGateway(context)


    val skillRegistry = SkillRegistry().apply {

        register(
            ClockSkill(clockGateway)
        )

        register(
            TimerSkill(timerGateway)
        )

        register(
            StopwatchSkill(stopwatchGateway)
        )

        register(
            CalendarSkill(calendarGateway)
        )

        register(
            PhoneSkill(phoneGateway)
        )

        register(
            ContactSkill(contactGateway)
        )

        register(
            AppsSkill(appGateway)
        )

    }

}