document.getElementById("orderform").addEventListener("submit",function(event){

    event.preventDefault();  // to wait on a console


const items = {
    burger: 120,
    pizza: 250,
    pasta: 180,
    coffee: 80
};

let orderInput = document.getElementById("orderInput").value; // Taken the values from an input box
let orderedArray = orderInput.split(",");
let order = []; // this is an empty array 

for(let i=0; i<orderedArray.length; i++)
{
    order.push(orderedArray[i].trim()); // it will remove the spaces and push the items in an array 
}
let total = 0;
for(let i=0;i<order.length;i++){
    let item = order[i];
    
    if(items[item] ){
        total = total + items[item];
    }
    else{
        console.log("Item is not in the list");
    }
}
let discount = 0;
console.log("Total price : " + total);
if(order.length > 3){
    discount = total * 0.10;
    console.log("Discount applied: " + discount);
}
const totalbill = total-discount;
const bill =  {

    itemsOrdered: order,  // array form
    discountApplied : discount,
    finalAmount : totalbill
}
console.log(JSON.stringify(bill,null,1));

});


