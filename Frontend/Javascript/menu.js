let menuItems = JSON.parse(localStorage.getItem("menuItems")) || [];


function apiCreateItem(item) {
  return new Promise((resolve) => {
    setTimeout(() => resolve("Item Created"), 300);
  });
}

function apiGetAllItems() {
  return new Promise((resolve) => {
    setTimeout(() => resolve(menuItems), 300);
  });
}

function saveToLocalStorage() {
  localStorage.setItem("menuItems", JSON.stringify(menuItems));
}

function Table() {
  const tableBody = document.getElementById("menuTable");
  tableBody.innerHTML = "";
  menuItems.forEach((item, index) => {
    let row = `
      <tr>
        <td>${item.name}</td>
        <td>${item.desc}</td>
        <td>${item.category}</td>
        <td>${item.price}</td>
        <td>${item.availability}</td>
        <td>
          <button class="btn edit" onclick="editItem(${index})">Edit</button>
          <button class="btn delete" onclick="deleteItem(${index})">Delete</button>
        </td>
      </tr>
    `;
    tableBody.innerHTML += row;
  });
}

document.getElementById("menuForm").addEventListener("submit", function(e) {
  e.preventDefault();

  let item = {
    name: document.getElementById("itemName").value,
    desc: document.getElementById("itemDesc").value,
    category: document.getElementById("itemCategory").value,
    price: document.getElementById("itemPrice").value,
    availability: document.getElementById("itemAvailability").value
  };

  let editIndex = document.getElementById("editIndex").value;

  if (editIndex === "") {
    // Add new
    apiCreateItem(item).then(() => {
      menuItems.push(item);
      saveToLocalStorage();
      Table();
    });
  } else {
    // Update
    menuItems[editIndex] = item;
    saveToLocalStorage();
    Table();
    document.getElementById("editIndex").value = "";
  }

  this.reset();
});

function editItem(index) {
  let item = menuItems[index];
  document.getElementById("itemName").value = item.name;
  document.getElementById("itemDesc").value = item.desc;
  document.getElementById("itemCategory").value = item.category;
  document.getElementById("itemPrice").value = item.price;
  document.getElementById("itemAvailability").value = item.availability;
  document.getElementById("editIndex").value = index;
}

function deleteItem(index) {
  menuItems.splice(index, 1);
  saveToLocalStorage();
  Table();
}

// Initial load
apiGetAllItems().then((items) => {
  menuItems = items;
  Table();
});