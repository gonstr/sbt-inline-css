/*global process, require */

(function () {
    "use strict";

    var args = process.argv,
        fs = require("fs"),
        inlineCss = require("inline-css"),
        mkdirp = require("mkdirp"),
        path = require("path");

    var SOURCE_FILE_MAPPINGS_ARG = 2;
    var TARGET_ARG = 3;
    var OPTIONS_ARG = 4;

    var sourceFileMappings = JSON.parse(args[SOURCE_FILE_MAPPINGS_ARG]);
    var target = args[TARGET_ARG];
    var options = JSON.parse(args[OPTIONS_ARG]);

    var sourcesToProcess = sourceFileMappings.length;
    var results = [];
    var problems = [];

    function parseDone() {
        if (--sourcesToProcess === 0) {
            console.log("\u0010" + JSON.stringify({results: results, problems: problems}));
        }
    }

    function throwIfErr(e) {
        if (e) throw e;
    }

    sourceFileMappings.forEach(function (sourceFileMapping) {
        var input = sourceFileMapping[0];
        var outputFile = sourceFileMapping[1].replace(".html", "inline.html");
        var output = path.join(target, outputFile);

        fs.readFile(input, "utf8", function (e, contents) {
            throwIfErr(e);

            inlineCss(contents, options, function (err, html) {

                mkdirp(path.dirname(output), function (e) {
                    throwIfErr(e);

                    fs.writeFile(output, html, "utf8", function (e) {
                        throwIfErr(e);

                        results.push({
                            source: input,
                            result: {
                                filesRead: [input],
                                filesWritten: [output]
                            }
                        });

                        parseDone();
                    });
                });
            });
        });
    });
})();