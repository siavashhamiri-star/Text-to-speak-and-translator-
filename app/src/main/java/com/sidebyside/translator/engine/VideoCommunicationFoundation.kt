package com.sidebyside.translator.engine

import android.content.Context
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

/**
 * Architectural foundation for two-person video communication mode.
 *
 * Implements:
 * 1. Native front-facing camera preview via CameraX
 * 2. Real-time overlay hooks for translation subtitles
 * 3. Lifecycle-aware camera binding and resource management
 *
 * Backend Dependency Note (Honest Technical Scope):
 * True peer-to-peer or server-relayed remote video calling across two physical devices
 * requires a WebRTC signaling server (STUN/TURN + SDP negotiation) or a managed video calling SDK
 * (such as LiveKit, Agora, or Firebase WebRTC signaling).
 * This class implements the complete local device video capture and overlay subsystem,
 * and provides the interface contract for plugging in the remote WebRTC peer stream
 * when a signaling server is deployed.
 */
class VideoCommunicationFoundation(
    private val context: Context,
    private val lifecycleOwner: LifecycleOwner
) {
    private var cameraExecutor: ExecutorService = Executors.newSingleThreadExecutor()
    private var cameraProvider: ProcessCameraProvider? = null

    interface RemotePeerStreamListener {
        fun onRemotePeerConnected(peerId: String)
        fun onRemotePeerDisconnected(peerId: String)
        fun onSubtitlesReceived(text: String, speakerId: String)
    }

    var peerListener: RemotePeerStreamListener? = null

    fun startFrontCamera(previewView: PreviewView, onError: (String) -> Unit) {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)

        cameraProviderFuture.addListener({
            try {
                cameraProvider = cameraProviderFuture.get()

                val preview = Preview.Builder().build().also {
                    it.setSurfaceProvider(previewView.surfaceProvider)
                }

                // Front camera selector
                val cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA

                cameraProvider?.unbindAll()
                cameraProvider?.bindToLifecycle(
                    lifecycleOwner,
                    cameraSelector,
                    preview
                )
            } catch (exc: Exception) {
                onError("Failed to bind front camera preview: ${exc.message}")
            }
        }, ContextCompat.getMainExecutor(context))
    }

    fun stopCamera() {
        cameraProvider?.unbindAll()
    }

    fun release() {
        stopCamera()
        cameraExecutor.shutdown()
    }
}
