# country-util
A small Java project exposing country enums with Locales

# The Country enum
The Country enum simply exposes the country names (in english) as well as their corresponding Java Locale (from which you can get the [ISO 3166](https://www.iso.org/iso-3166-country-codes.html) two letter country code).

```java
public enum Country{

	ANDORRA(new Locale("AD")),
	AFGHANISTAN(new Locale("AF")),
	ANTIGUA_AND_BARBUDA(new Locale("AG")),
	ANGUILLA(new Locale("AI")),
        //etc
	ZAMBIA(new Locale("ZM")),
	ZIMBABWE(new Locale("ZW"));

	private Locale locale;

	private Country(Locale locale){
		this.locale = locale;
	}

	public Locale getLocale(){
		return locale;
	}
```
