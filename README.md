# Pixabay Image Search App
Pixabay Image search App using SOLID architecture, uses Hilt, Room, Compose, Coroutines, Retrofit, www.pixabay.com API

<!--
[<img src="https://user-images.githubusercontent.com/5157474/171551314-c0e1dd15-310a-4808-9c25-1be2749a0980.png" width="350"/>](https://user-images.githubusercontent.com/5157474/171551314-c0e1dd15-310a-4808-9c25-1be2749a0980.png)
-->
[<img src="https://user-images.githubusercontent.com/5157474/195039930-fee067f3-734f-4417-8226-950850f68699.png" width="350"/>](https://user-images.githubusercontent.com/5157474/195039930-fee067f3-734f-4417-8226-950850f68699.png)
[<img src="https://user-images.githubusercontent.com/5157474/195039958-b935c659-236f-4618-80ac-25060fa6dc11.png" width="350"/>](https://user-images.githubusercontent.com/5157474/195039958-b935c659-236f-4618-80ac-25060fa6dc11.png)
[<img src="https://user-images.githubusercontent.com/5157474/195040048-cc4c6638-40f4-45a1-889e-a9bd49a61f4b.png" width="350"/>](https://user-images.githubusercontent.com/5157474/195040048-cc4c6638-40f4-45a1-889e-a9bd49a61f4b.png)
[<img src="https://user-images.githubusercontent.com/5157474/195040001-27e7bcf8-b2e8-4cb9-90bb-2a91cde283b2.png" width="350"/>](https://user-images.githubusercontent.com/5157474/195040001-27e7bcf8-b2e8-4cb9-90bb-2a91cde283b2.png)


- Allows users to search for images by name
- Images and Search results cached for offline use
- Infinity scrolling only loads the search results that you scroll to

---- Tech used ----
- List of Images and Search results are cached locally in a Room database
- Uses custom query for Room database
- Shows proper use of Dagger-Hilt & SOLID architecture
- Uses compose for view layer, ViewModel and mutableState to send UI events
- Infinite scroll using Compose (not recyclerview)
- Uses PhotoView in an AndroidView from Compose
- Uses Resource sealed class to handle errors and messaging

To install the Apk:

1. Open this link on your Android device:
   https://github.com/realityexpander/PixabayApp/blob/master/PixabayApp_1.0.apk
2. Tap the "skewer" menu and tap the "download"

   [![](https://user-images.githubusercontent.com/5157474/147434050-57102a30-af32-46ed-a90b-d94e0c4a4f35.jpg)]()
3. Allow the file to download (DO NOT click "show details")
4. After the file is downloaded, click "OK" to install
5. Click "OK" to install
6. Click "OK" to launch

If you have developer options turned on, you may need to turn off "USB Debugging" if the "Waiting for debugger" dialog is displayed.
