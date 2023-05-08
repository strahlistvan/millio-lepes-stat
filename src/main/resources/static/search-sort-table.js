/* Table sorting and searching implementation in Vanilla.js
   Based on: 
     - https://www.w3schools.com/howto/howto_js_filter_table.asp 
     - https://www.w3schools.com/howto/howto_js_sort_table.asp
     - https://github.com/tofsjonas/sortable
*/

function displayFilteredRow(tr, filter, colIndex) {
  var td, txtValue;
  
  td = tr.getElementsByTagName("td")[colIndex];
  if (td) {
    txtValue = td.textContent || td.innerText;
    if (txtValue.toUpperCase().indexOf(filter) > -1) {
      tr.style.display = "";
	} 
  }

}

function searchInTable() {
  // Declare variables
  var input, filter, table, tr, i, j, colIdxList;
  input = document.getElementById("searchInput");
  filter = input.value.toUpperCase();
  table = document.getElementById("schoolTable");
  trList = table.getElementsByTagName("tr");
  colIdxList = [0,1,2,3];

  // Loop through all table rows, and hide those who don't match the search query
  // First row contains header - skip i = 0
  for (i = 1; i < trList.length; i++) {
    trList[i].style.display = "none";
     
    // search by all criteria column
    for (j = 0; j < colIdxList.length; j++) {
	  displayFilteredRow(trList[i], filter, j);
    }
  }
}

//Sorting is not in the script, thanks for this repository: https://github.com/tofsjonas/sortable
