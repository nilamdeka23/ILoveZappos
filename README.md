# ILoveZappos
Zappos Coding Challenge
###### One can download a working signed apk of this app from [here](https://github.com/nilamdeka23/ILoveZappos/blob/master/release_apk/ilovezappos.apk?raw=true)


### What my app does?
1. Accepts a search query from the user.
2. Makes a request to the Zappos API with the search input from the user.
3. Parses the response and displays the first product in a CardView
4. The "Add to Cart", floating action button animates to inform the user if a product is added.

### Application flow
![alt tag](https://github.com/nilamdeka23/ILoveZappos/blob/master/screenshots/ILoveZappos.gif)
###### I tried to follow the Material Design pattern in available time.
###### One can find additional screenshots [here](https://github.com/nilamdeka23/ILoveZappos/blob/master/screenshots/)

### Implementation Details
To maintain a good quality of code, I have used the upcoming MVVM architectural pattern 

I created three main packages: Model, View and ViewModel.


### Major Libraries used
* **RxJava + RxAndroid**: A way to work asynchronously and maintain the application scalable.
* **Retrofit** : For consuming Rest API.
* **Picasso**: For image loading.
