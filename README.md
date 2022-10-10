# <h4>News Update</h4>

## News Update is provide latest news feeds list and notification.

### Architecture and Tech-stack
* Target SDK Version - 29
* Minimum SDK version - 16
* Built on MVP architecture pattern
* Custom [NetworkUtility](https://github.com/rahulsinghfaujdar/NewsDetail/blob/main/app/src/main/java/com/newsdetail/network/NetworkAdapter.java)
* Not used OkHttp/Gson/Retrofit/Volly
* Uses [Glide](https://github.com/bumptech/glide) for image loading.
* Uses Recycler View & Card View
* Uses Constraint Layout
* Uses Custom Fonts [Nunito](https://github.com/rahulsinghfaujdar/NewsDetail/tree/main/app/src/main/res/font)
* Uses Static API
* Send Firebase Push Notification available in [BigPictureStyle](https://github.com/rahulsinghfaujdar/NewsDetail/blob/main/app/src/main/java/com/newsdetail/utility/Firebase/NotificationTemplateUtility.java)

### Features
* Searching on news listed items.
* Sorting according to publish date.
* Day & Night Mode theme toggle selection available every time [Default Mode = Day Mode]
* Provide In-App Browsing to read more details for specific news.

### Firebase Push Data Payload Keys
| Key | Value | Use | 
| --- | --- | --- |
| `title` | `String` | **(required)**  News Title
| `description` | `String` | **(required)**  News Description 
| `urlToImage` | *String* | **(required)**  News Image
| `url` | *String* | **(required)**  News Browsable Link

### Screenshots
<img src="https://github.com/rahulsinghfaujdar/NewsDetail/blob/main/screenshots/Screenshot_20221006_130541.png" width="25%"></img> <img src="https://github.com/rahulsinghfaujdar/NewsDetail/blob/main/screenshots/Screenshot_20221006_130620.png" width="25%"></img> <img src="https://github.com/rahulsinghfaujdar/NewsDetail/blob/main/screenshots/Screenshot_20221006_130653.png" width="25%"></img>
