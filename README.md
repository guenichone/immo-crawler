
# Immo-Crawler

## Description
Immo Crawler is a project made to help you for your Real Estate investigations.

Other crawler are giving you access to a part only of the announces on the web.
Also you cannot manipulate those data, or simply get the subset you want and as fresh as possible.
Leading you to check multiple website and manually browsing again and again the sames announces.

Based on this Immo Crawler is born to be able to find the exact goods for you with less efforts as possible.

## Project content
- **batch** : Website crawler that browse differents Real Estate websites and push the data in DB.
- **backend** : Backend that exposes rest API to read and manipulate the data.
- **frontend** : Simple web page used to view and maniulate the data.
- **database** : Shared module between batch and backend used for model and database.

## Get started

### Crawler

Download gecko driver to allow Selenium to use Firefox : https://github.com/mozilla/geckodriver/releases

Edit **application.properties**
- To set your own MongoDB connection : **spring.data.mongodb.<...>**
- To set your gecko driver path : **webdriver.gecko.driver**

Edit **ImmoCrawlerBatch** to set your own search criterias.

Run the **ImmoCrawlerBatch** java class.

That's it ! You can follow the logs to see the database filled with your results.

### Website

Edit **application.properties** to set your own MongoDB connection.

Run the **ImmoCrawlerBackendApp** java class. (Will run server on port **9090**)

Run *npm start* from the **frontend**.

That's it ! You can connect localhost:4200 with a brower and use your data.

# Known bugs

## Seloger.com connection issue

Issue with the connection which fails very often.

There is no solution yet except to relaunch the crawler later.