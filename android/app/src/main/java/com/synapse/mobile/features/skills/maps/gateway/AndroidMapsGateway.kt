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
import android.os.Looper
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.Priority
import android.os.HandlerThread
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
class AndroidMapsGateway(
    private val context: Context
) : MapsGateway {

    override suspend fun getCurrentLocation(): String? {

        val fineGranted =
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED

        val coarseGranted =
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED

        if (!fineGranted && !coarseGranted) {
            return "Location permission not granted."
        }

        val fusedClient =
            LocationServices.getFusedLocationProviderClient(context)

        return try {

            val location =
                suspendCancellableCoroutine<android.location.Location?> { continuation ->

                    fusedClient.lastLocation

                        .addOnSuccessListener { location ->

                            android.util.Log.d(
                                "SYNAPSE_LOCATION",
                                "Last location result = $location"
                            )

                            if (continuation.isActive) {
                                continuation.resume(location)
                            }
                        }

                        .addOnFailureListener { exception ->

                            android.util.Log.e(
                                "SYNAPSE_LOCATION",
                                "Location request failed",
                                exception
                            )

                            if (continuation.isActive) {
                                continuation.resume(null)
                            }
                        }
                }

            if (location != null) {

                getAddressFromLocation(
                    location.latitude,
                    location.longitude
                )

            } else {

                "Unable to get current location."
            }

        } catch (e: Exception) {

            android.util.Log.e(
                "SYNAPSE_LOCATION",
                "Location error",
                e
            )

            "Unable to get current location."
        }
    }

    private fun getAddressFromLocation(
        latitude: Double,
        longitude: Double
    ): String {

        return try {

            val geocoder =
                Geocoder(
                    context,
                    Locale.getDefault()
                )

            val addresses =
                geocoder.getFromLocation(
                    latitude,
                    longitude,
                    1
                )

            if (!addresses.isNullOrEmpty()) {

                addresses[0].getAddressLine(0)

            } else {

                "Lat: $latitude, Lng: $longitude"
            }

        } catch (e: Exception) {

            e.printStackTrace()

            "Lat: $latitude, Lng: $longitude"
        }
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

            val uri = Uri.parse(
                "google.navigation:q=${Uri.encode(destination)}"
            )

            val intent = Intent(
                Intent.ACTION_VIEW,
                uri
            ).apply {

                setPackage("com.google.android.apps.maps")

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
        origin: String,
        destination: String
    ): Boolean {

        return try {

            val uri = Uri.parse(
                "https://www.google.com/maps/dir/?api=1" +
                        "&origin=${Uri.encode(origin)}" +
                        "&destination=${Uri.encode(destination)}"
            )

            val intent = Intent(
                Intent.ACTION_VIEW,
                uri
            ).apply {

                setPackage("com.google.android.apps.maps")

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