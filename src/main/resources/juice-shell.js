var fs = require("fs"),
    jst = require("jstranspiler"),
    nodefn = require("when/node"),
    mkdirp = require("mkdirp"),
    path = require("path"),
    juice = require("juice");

var promised = {
    mkdirp: nodefn.lift(mkdirp),
    readFile: nodefn.lift(fs.readFile),
    writeFile: nodefn.lift(fs.writeFile)
};

var args = jst.args(process.argv);

function processor(input, output) {

    return promised.readFile(input, "utf8").then(function(contents) {
        return juice(contents, args.options);
    }).then(function(result) {
        return promised.mkdirp(path.dirname(output)).yield(result);
    }).then(function(result) {
        return promised.writeFile(output, result, "utf8").yield(result);
    }).then(function(result) {
        return {
            source: input,
            result: {
                filesRead: [input],
                filesWritten: [output]
            }
        };
    });
}

jst.process({processor: processor, inExt: ".html", outExt: ".inline.html"}, args);