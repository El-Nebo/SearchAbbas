import 'dart:math';
import 'package:flutter/material.dart';
import 'package:search_engine/DB/Controller.dart';
import 'package:search_engine/NavBar.dart';
import 'package:search_engine/SearckBar.dart';
import 'package:search_engine/main.dart';
import './DB/Result.dart';
import 'package:url_launcher/url_launcher.dart';

class ResultsScreen extends StatefulWidget {
  final String query;
  ResultsScreen({Key key, this.query, this.pageNumber, this.maxIndex})
      : super(key: key);
  final int pageNumber;
  int maxIndex = 2;
  @override
  _ResultsScreenState createState() => _ResultsScreenState();
}

class _ResultsScreenState extends State<ResultsScreen> {
  Future<List<ResultElement>> results;
  @override
  void initState() {
    super.initState();
    results = getResults(super.widget.query);
  }

  @override
  Widget build(BuildContext context) {
    Size screenSize = MediaQuery.of(context).size;

    return SafeArea(
      child: Scaffold(
        body: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Expanded(
              flex: 1,
              child: Padding(
                padding: EdgeInsets.all(screenSize.height / 60),
                child: (screenSize.width > 500)
                    ? Row(
                        children: [
                          InkWell(
                              onTap: () => Navigator.of(context).push(
                                  MaterialPageRoute(
                                      builder: (_) => MyHomePage())),
                              child: Image.asset("images/logo.png")),
                          Container(
                            padding: EdgeInsets.symmetric(
                                horizontal: screenSize.height / 30),
                            constraints: BoxConstraints(
                                maxWidth: min(screenSize.width / 2, 600)),
                            child: SearchBar(),
                          ),
                        ],
                      )
                    : Row(
                        children: [
                          InkWell(
                              onTap: () => Navigator.of(context).push(
                                  MaterialPageRoute(
                                      builder: (_) => MyHomePage())),
                              child: Container(
                                child: Image.asset("images/logo.png"),
                                constraints: BoxConstraints(
                                    maxWidth: min(screenSize.width / 4, 600)),
                              )),
                          Container(
                            padding: EdgeInsets.symmetric(
                                horizontal: screenSize.height / 30),
                            constraints: BoxConstraints(
                                maxWidth: min(screenSize.width * 6 / 9, 600)),
                            child: SearchBar(),
                          ),
                        ],
                      ),
              ),
            ),
            Divider(),
            Expanded(
              flex: 8,
              child: Container(
                child: FutureBuilder<List<ResultElement>>(
                  future: results,
                  builder:
                      (context, AsyncSnapshot<List<ResultElement>> snapshot) {
                    if (snapshot.hasData) {
                      if (snapshot.data.length > 0) {
                        return ListView.builder(
                            itemCount: snapshot
                                .data.length, //min(10, snapshot.data.length),
                            itemBuilder: (context, ind) {
                              int index = ind + super.widget.pageNumber;
                              return ListTile(
                                title: Column(
                                  crossAxisAlignment: CrossAxisAlignment.start,
                                  children: [
                                    InkWell(
                                      child: Text(
                                        snapshot.data[index].title,
                                        style: TextStyle(
                                            color: Colors.blue,
                                            decoration:
                                                TextDecoration.underline),
                                      ),
                                      onTap: () =>
                                          launch(snapshot.data[index].url),
                                    ),
                                    Text(
                                      snapshot.data[index].url,
                                      style:
                                          TextStyle(color: Colors.green[300]),
                                    ),
                                    Divider()
                                  ],
                                ),
                              );
                            });
                      } else
                        return Container(
                            child: Text(
                                "No Result found Please Try another word "));
                    } else if (snapshot.connectionState ==
                        ConnectionState.waiting)
                      return Container(child: Text("Loading...."));
                    else
                      return Container(
                          child: Text("Ooops seems like we have an error"));
                  },
                ),
              ),
            ),
          ],
        ),
        // bottomNavigationBar: PagesNavBar(
        //   maxPage: super.widget.maxIndex,
        //   pageNumber: super.widget.pageNumber,
        //   query: super.widget.query,
        // ),
      ),
    );
  }
}
