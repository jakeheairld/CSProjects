using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class PlayerCamera : MonoBehaviour
{
    private Alteruna.Avatar _avatar;
    private Camera _playerCamera;
    private AudioListener _audioListener;

    void Awake() // Changed from Start() to Awake()
    {
        _avatar = GetComponent<Alteruna.Avatar>();
        _playerCamera = GetComponentInChildren<Camera>(true); // Include inactive children
        _audioListener = GetComponentInChildren<AudioListener>(true);

        if (_avatar.IsMe)
        {
            // Disable the scene's main camera
            if (Camera.main != null)
                Camera.main.gameObject.SetActive(false);

            // Enable this player's camera and audio
            _playerCamera.enabled = true;
            _audioListener.enabled = true;
        }
        else
        {
            _playerCamera.enabled = false;
            _audioListener.enabled = false;
        }
    }
}