# relic
[![Build Status](https://app.travis-ci.com/furplag/relic.svg?branch=master)](https://app.travis-ci.com/furplag/relic)
[![Coverage Status](https://coveralls.io/repos/github/furplag/relic/badge.svg?branch=master)](https://coveralls.io/github/furplag/relic?branch=master)
[![Codacy Badge](https://app.codacy.com/project/badge/Grade/8ef45ed49f824454ac3c51e279c64be6)](https://www.codacy.com/gh/furplag/relic/dashboard?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=furplag/relic&amp;utm_campaign=Badge_Grade)
[![codebeat badge](https://codebeat.co/badges/6646361b-98a5-4b8e-ba2b-b7795169b23a)](https://codebeat.co/projects/github-com-furplag-relic-master)
[![Maintainability](https://api.codeclimate.com/v1/badges/d2f869d3c736a8c155d9/maintainability)](https://codeclimate.com/github/furplag/relic/maintainability)

hope those snippets will help us for development .

## Getting Start

Add the following snippet to any project's pom that depends on your project
```pom.xml
<repositories>
  ...
  <repository>
    <id>relic</id>
    <url>https://raw.github.com/furplag/relic/mvn-repo/</url>
    <snapshots>
      <enabled>true</enabled>
      <updatePolicy>always</updatePolicy>
    </snapshots>
  </repository>
</repositories>
...
<dependencies>
  ...
  <dependency>
    <groupId>jp.furplag.sandbox</groupId>
    <artifactId>relic</artifactId>
    <version>5.0.2</version>
  </dependency>
</dependencies>
```

## Feauture

### Reflections
enable to unsafe access to the Object .
### Streamr
shorthands to use stream API .
### Trebchet
functional interfaces which against some problems when handling exceptions in lambda expression .
### Deamtiet
converting between each instant values represented by some of (different) time scale,
e.g.) epoch millis, unix date, julian date and something .
### Commonizr
optimized Unicode character(s) normalization for using under standard input .
### Localizr
shorthands of Locale and Timezone .
## License
Code is under the [Apache License v2](LICENSE).
