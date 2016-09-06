//CRAWLING using casper
var casper = require('casper').create();

casper.start('http://casperjs.org/', function() {
    this.echo(this.getTitle());
});

casper.thenOpen('http://phantomjs.org', function() {
    this.echo(this.getTitle());
});

casper.run();

//using crawler 
/*var crawler = require("crawler").Crawler;

var c = new Crawler({
	"maxConnections": 10,

	"callback": function(error, result, $) {
		if(result) {
			var page = result.body;
			var res = page.match(/bechtel/i);
			if(res && res.length > 0) {
				console.log(result.body);
			}
		}

		$("a").each(function(index,a) {
			console.log(a.href);
			c.queue(a.href);
		});
	}
});

c.queue("http://construction.com");*/