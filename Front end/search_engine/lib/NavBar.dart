import 'dart:math';

import 'package:flutter/material.dart';
import 'package:search_engine/resultsScreen.dart';

class PagesNavBar extends StatelessWidget {
  PagesNavBar({Key key, this.query, this.tiles, this.pageNumber, this.maxPage})
      : super(key: key);
  final int pageNumber;
  final int maxPage;
  int tiles;
  String query;
  @override
  Widget build(BuildContext context) {
    return Row(
      children: [
        FloatingActionButton(
          backgroundColor:
              (pageNumber == 0) ? Colors.blue[100] : Colors.blue[100],
          child: Icon(Icons.arrow_back_sharp),
          onPressed: () {
            if (pageNumber > 0)
              Navigator.of(context).push(
                MaterialPageRoute(
                  builder: (_) => ResultsScreen(
                    maxIndex: maxPage,
                    pageNumber: pageNumber - 1,
                    query: query,
                  ),
                ),
              );
          },
        ),
        Text(maxPage.toString()),
        FloatingActionButton(
          backgroundColor:
              (pageNumber == 0) ? Colors.blue[100] : Colors.blue[100],
          child: Icon(Icons.arrow_forward_sharp),
          onPressed: () {
            if (pageNumber < maxPage)
              Navigator.of(context).push(
                MaterialPageRoute(
                  builder: (_) => ResultsScreen(
                    maxIndex: maxPage,
                    pageNumber: pageNumber + 1,
                    query: query,
                  ),
                ),
              );
          },
        ),
      ],
    );
  }
}
