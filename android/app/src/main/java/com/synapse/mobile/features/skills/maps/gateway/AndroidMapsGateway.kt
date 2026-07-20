package com.synapse.mobile.features.skills.maps.gateway

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.net.Uri
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices
import java.util.Locale
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

class AndroidMapsGateway(
    private val context: Context
) : MapsGateway {

    override fun getCurrentLocation(): String? {

        if (
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return "Location permission not granted."
        }

        val fusedClient =
            LocationServices.getFusedLocationProviderClient(context)

        var result: String? = null

        val latch = CountDownLatch(1)

        fusedClient.lastLocation
            .addOnSuccessListener { location ->

                if (location == null) {

                    result = "Unable to get location."

                    latch.countDown()

                    return@addOnSuccessListener
                }

                try {

                    val geocoder =
                        Geocoder(
                            context,
                            Locale.getDefault()
                        )

                    val addresses =
                        geocoder.getFromLocation(
                            location.latitude,
                            location.longitude,
                            1
                        )

                    result =
                        if (
                            !addresses.isNullOrEmpty()
                        ) {
                            addresses[0].getAddressLine(0)
                        } else {
                            "Lat: ${location.latitude}\nLng: ${location.longitude}"
                        }

                } catch (e: Exception) {

                    result =
                        "Lat: ${location.latitude}\nLng: ${location.longitude}"

                }

                latch.countDown()

            }
            .addOnFailureListener {

                result = "Unable to fetch location."

                latch.countDown()

            }

        latch.await(
            5,
            TimeUnit.SECONDS
        )

        return result

    }

    override fun openMaps(
        query: String
    ): Boolean {

        return try {

            val uri =
                Uri.parse(
                    "geo:0,0?q=${Uri.encode(query)}"
                )

            val intent =
                Intent(
                    Intent.ACTION_VIEW,
                    uri
                ).apply {

                    setPackage(
                        "com.google.android.apps.maps"
                    )

                    addFlags(
                        Intent.FLAG_ACTIVITY_NEW_TASK
                    )

                }

            context.startActivity(intent)

            true

        } catch (e: Exception) {

            e.printStackTrace()

            false

        }

    }

    override fun navigate(
        destination: String
    ): Boolean {

        return try {

            val uri =
                Uri.parse(
                    "google.navigation:q=${Uri.encode(destination)}"
                )

            val intent =
                Intent(
                    Intent.ACTION_VIEW,
                    uri
                ).apply {

                    setPackage(
                        "com.google.android.apps.maps"
                    )

                    addFlags(
                        Intent.FLAG_ACTIVITY_NEW_TASK
                    )

                }

            context.startActivity(intent)

            true

        } catch (e: Exception) {

            e.printStackTrace()

            false

        }

    }

    override fun searchPlace(
        place: String
    ): Boolean {

        return openMaps(place)

    }

}