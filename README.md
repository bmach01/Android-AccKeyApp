# AcKey

**AcKey** is a proof of concept mobile client application developed in Kotlin. It acts as the front-end for the AcKey project, which communicates with the back-end server [AcKeyServer](https://github.com/bmach01/AcKeyServer).

## Features

- **User Registration**: Register with the URL of your home server and a One-Time Password (OTP).
- **User Authentication**: Log in securely using a custom PIN or your phone's built-in authentication system (biometrics, face unlock, etc.).
- **Credential Storage & Security**: Securely fetches and stores user credentials locally during registration using the Android KeyStore.
- **JWT Token Management**: Obtains a JWT token during login, which is used for all API calls. Tokens are automatically refreshed upon expiration.
- **Barcode/QR Code Generation**: Fetches a code from the server and generates a Barcode or QR code to display to the user.
- **Settings**: Manage your login method (PIN or biometric) and account within the app.
- **Automatic Code Refresh**: The app automatically fetches a new code when the previous one expires.

## Tech Stack

- **Kotlin**
- **Jetpack Compose** - For building the user interface.
- **DataStore** - Local data persistence.
- **KeyStore** - Secure storage for encryption keys.
- **Ktor** - For client-server communication.
- **Hilt** - Dependency Injection

## How It Works

1. **Registration**: Users must first register by providing the URL of their home server and a valid OTP. During this process, credentials are securely fetched from the server and stored locally using the KeyStore.
2. **Authentication**: After registration, users can log in using either a predefined PIN or phone authentication (fingerprint, face unlock, etc.).
3. **JWT Management**: Upon login, the app fetches a JWT token which is used to authenticate future API calls. The token is automatically refreshed when it expires (app is supposed to be used in short spans with long intervals so refresh token was resigned upon).
4. **Code Fetching & Barcode/QR Code Display**: The app periodically fetches a short lived keycode from the server, which it uses to generate a Barcode or QR code that is presented to the user. When the code expires, the app will automatically fetch a new one.
5. **Settings**: Users can configure the login method (PIN or biometric) and manage their account details within the app.
