# ShopAssistantXML
XML serialization android library using the [SimpleXML](https://github.com/ngallagher/simplexml) framework for the [Shop Assistant](https://play.google.com/store/apps/details?id=pnapp.productivity.store) project

The library is published on bintray and can be accessed with the next code:

```gradle
allprojects {
    repositories {
        jcenter()
        maven {
            url "http://dl.bintray.com/pnaleks/pnapp/"
        }
    }
}

dependencies {
    ...
    compile 'ru.pnapp:sa-xml:1.0.5@aar'
    compile('org.simpleframework:simple-xml:2.7.1') {
        exclude module: 'stax'
        exclude module: 'stax-api'
        exclude module: 'xpp3'
    }
}
```
