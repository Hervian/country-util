# country-util
A small project exposing a Country enum

# The Country enum
The Country enum simply exposes the country names (in english) as well as their corresponding Java Locale (from which you can get the [ISO 3166](https://www.iso.org/iso-3166-country-codes.html) two letter country code).

```java
public enum Country{

	ANDORRA(new Locale("AD")),
	UNITED_ARAB_EMIRATES(new Locale("AE")),
	AFGHANISTAN(new Locale("AF")),
	ANTIGUA_AND_BARBUDA(new Locale("AG")),
	ANGUILLA(new Locale("AI")),
	ALBANIA(new Locale("AL")),
	ARMENIA(new Locale("AM")),
  //etc
```
