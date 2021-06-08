import 'dart:convert';

class Result {
    Result({
        this.result,
    });

    List<ResultElement> result;

    factory Result.fromJson(String str) => Result.fromMap(json.decode(str));

    String toJson() => json.encode(toMap());

    factory Result.fromMap(Map<String, dynamic> json) => Result(
        result: List<ResultElement>.from(json["result"].map((x) => ResultElement.fromMap(x))),
    );

    Map<String, dynamic> toMap() => {
        "result": List<dynamic>.from(result.map((x) => x.toMap())),
    };
}

class ResultElement {
    ResultElement({
        this.url,
        this.title,
    });

    String url;
    String title;

    factory ResultElement.fromJson(String str) => ResultElement.fromMap(json.decode(str));

    String toJson() => json.encode(toMap());

    factory ResultElement.fromMap(Map<String, dynamic> json) => ResultElement(
        url: json["URL"],
        title: json["Title"],
    );

    Map<String, dynamic> toMap() => {
        "URL": url,
        "Title": title,
    };
}

// class Result {
//   final String url;
//   final String title;

//   Result(this.url, this.title);
//   String get getUrl => this.url;
//   String get getTitle => this.url;
//   @override
//   String toString() {
//     return 'URL : ${url}   Title: ${title}';
//   }
// }
