> Note that this plugin is presently only working with an engineType set to Node e.g.:
> `set JsEngineKeys.engineType := JsEngineKeys.EngineType.Node`

# sbt-inline-css
sbt-web plugin for inlining css in html files.

Uses the [juice][1] node plugin https://github.com/Automattic/juice

[1]: https://github.com/Automattic/juice

# How to use

This plugin requires Node to be installed and the 'juice' package to be available at runtime. To install juice
with npm:

```bash
npm install juice
```

In your build file add an include filter for the files you want to inline:

```scala
includeFilter in (Assets, InlineCssKeys.inlineCss) := "test.html"
```

This will generate files where 'inline.' is added to the file extension; e.g. test.inline.html will be generated
for a file named test.html

Add `inlineCss` to your assets pipeline:

```scala
pipelineStages := Seq(inlineCss)
```
