# ILoveZappos
Zappos Coding Challenge
######One can download a working signed apk of this app from [here](https://github.com/nilamdeka23/ILoveZappos/blob/master/release_apk/ilovezappos.apk?raw=true)

###What does my design do?
1. Accept a search query from the user.
2. Makes a request to the Zappos API with the search input from the user.
3. Parses the response and displays the first product in a CardView
4. The "Add to Cart", floating action button animates to inform the user a product is added.


###Implementation Details
To maintain a good quality of code, I have used the upcoming MVVM architectural pattern 
I created three main packages: Model, View and ViewModel.


###Screenshots/UI
![alt tag](https://github.com/nilamdeka23/ILoveZappos/blob/master/screenshots/1.PNG)
![alt tag](https://github.com/nilamdeka23/ILoveZappos/blob/master/screenshots/2.PNG)
![alt tag](https://github.com/nilamdeka23/ILoveZappos/blob/master/screenshots/3.PNG)
######I tried to follow the Material Design pattern in available time.


###Major Libraries used
* **RxJava + RxAndroid**: A way to work asynchronously and maintain the application scalable.
* **Retrofit** : For consuming Rest API.
* **Picasso**: For image loading.

