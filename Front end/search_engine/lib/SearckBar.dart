import 'package:flutter/material.dart';
import 'package:flutter_typeahead/flutter_typeahead.dart';
import 'package:search_engine/resultsScreen.dart';
import 'package:shared_preferences/shared_preferences.dart';
import 'constants.dart';

class SearchBar extends StatelessWidget {
  SearchBar({
    Key key,
  }) : super(key: key);

  void addToPref(String s) async {
    SharedPreferences prefs = await SharedPreferences.getInstance();
    var list = prefs.getStringList('key');

    if (!prefs.containsKey('key'))
      prefs.setStringList('key', [s.toLowerCase()]);

    if (list.contains(s.toLowerCase())) return;
    list.add(s);
    list.forEach((element) {
      debugPrint(element);
    });
    prefs.setStringList('key', list);
  }

  Future<List<String>> getAllprefs(String s) async {
    SharedPreferences prefs = await SharedPreferences.getInstance();

    if (s == "") return prefs.getStringList('key');

    List<String> suggestions = [];
    prefs.getStringList('key').forEach((element) {
      if (element.contains(RegExp("^" + s, caseSensitive: false)))
        suggestions.add(element);
    });
    return suggestions;
  }

  void viewResult(BuildContext context) {
    Navigator.of(context).push(MaterialPageRoute(
        builder: (context) => ResultsScreen(
              query: _typeAheadController.text.toLowerCase(),
              pageNumber: 0,
            )));
  }

  final TextEditingController _typeAheadController = TextEditingController();
  @override
  Widget build(BuildContext context) {
    return Container(
      padding: EdgeInsets.symmetric(horizontal: 20, vertical: 2),
      decoration: BoxDecoration(
        borderRadius: BorderRadius.circular(30),
        color: SEARCH_COLOR,
      ),
      child: TypeAheadField(
        suggestionsCallback: (input) => getAllprefs(_typeAheadController.text),
        itemBuilder: (BuildContext context, itemData) {
          return ListTile(
            tileColor: SEARCH_COLOR,
            title: Text(
              itemData,
              style: Theme.of(context)
                  .textTheme
                  .bodyText1
                  .copyWith(color: SEARCH_TEXT_COLOR),
            ),
          );
        },
        noItemsFoundBuilder: (context) => Text("No Suggestions Found"),
        onSuggestionSelected: (suggestion) =>
            _typeAheadController.text = suggestion,
        textFieldConfiguration: TextFieldConfiguration(
          onEditingComplete: () {
            addToPref(_typeAheadController.text);
            viewResult(context);
          },
          controller: this._typeAheadController,
          style: Theme.of(context)
              .textTheme
              .bodyText1
              .copyWith(color: SEARCH_TEXT_COLOR),
          decoration: InputDecoration(
              icon: Icon(
                Icons.search,
                color: Colors.blue,
              ),
              hintText: 'Search...',
              hintStyle: Theme.of(context)
                  .textTheme
                  .subtitle1
                  .copyWith(color: Colors.black45),
              border: InputBorder.none),
        ),
      ),
    );
  }
}
