import 'dart:math';

import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:search_engine/constants.dart';
import 'SearckBar.dart';

void main() {
  runApp(MyApp());
}

class MyApp extends StatelessWidget {
  // This widget is the root of your application.
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: PAGE_NAME,
      debugShowCheckedModeBanner: false,
      darkTheme: ThemeData.dark(),
      home: MyHomePage(),
    );
  }
}

class MyHomePage extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    Size screenSize = MediaQuery.of(context).size;
    double kheight = screenSize.height / 10;
    double kWidth = screenSize.width / 10;

    return Scaffold(
      body: Center(
          child: Container(
        padding: EdgeInsets.symmetric(horizontal: kWidth * .5),
        constraints: BoxConstraints(maxWidth: max(screenSize.width / 2, 600)),
        child: Column(
          children: [
            SizedBox(
              height: kheight * 2,
            ),
            Image.asset('images/logo.png'),
            SizedBox(
              height: kheight * .5,
            ),
            SearchBar(),
          ],
        ),
      )),
    );
  }
}
