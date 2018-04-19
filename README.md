# country-util
A small Java project exposing country enums with Locales

# The Country enum
The Country enum simply exposes the country names (in english) as well as their corresponding Java Locales (one for each of the given country's official languages), the [ISO 3166](https://www.iso.org/iso-3166-country-codes.html) two letter country code and a getFlag method, which returns an SVG image of the country's flag.  
The enum class also exposes a static fromIso3166CountryCode method.
NB: currently the following countries does not have any flag associated (I got an error due to some problems with the SVG images I quick solved it by simply deleting those images...  
TODO: Add flags for those countries + a unit test that fails if any getFlag method returns null. Missing flags in time of writing: fk.svg, gs.svg, gt.svg, ni.svg, pm.svg, pn.svg, pr.svg, zw.svg)

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
