class AppProvider{
  String printerAddress = '';
  String printerName = '';

  void updateNames(String printerAddressGotten, String printerNameGotten){
    printerAddress = printerAddressGotten;
    printerName = printerNameGotten;
  }
}