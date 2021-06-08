import 'dart:convert';

import 'package:http/http.dart' as http;
import 'Result.dart';

Future<List<ResultElement>> getResults(String query) async {
  Result result = Result.fromMap(jsonDecode(
      (await http.get(Uri.parse("http://localhost:3000/search/" + query)))
          .body));
  return result.result;
}
