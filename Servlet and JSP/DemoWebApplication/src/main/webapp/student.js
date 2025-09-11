document.getElementById("reportForm").addEventListener("submit", function(event) {
    event.preventDefault();

    let name = document.getElementById("studentName").value.trim();
    let marksInput = document.getElementById("marksInput").value.trim();
    let marksArray = marksInput.split(",").map(Number);

    if (marksArray.length !== 4 || marksArray.some(isNaN)) {
        alert("Please enter exactly 4 valid numbers separated by commas.");
        return;
    }

    let total = marksArray.reduce((a, b) => a + b, 0);
    let average = (total / 4).toFixed(2);
    let grade = average >= 90 ? 'A+' : average >= 80 ? 'A' : average >= 70 ? 'B' : average >= 60 ? 'C' : 'F';

    document.getElementById("output").innerHTML = `
        <h4>Report Card</h4>
        <p><strong>Name:</strong> ${name}</p>
        <p><strong>Marks:</strong> Math: ${marksArray[0]}, Science: ${marksArray[1]}, English: ${marksArray[2]}, History: ${marksArray[3]}</p>
        <p><strong>Total Marks:</strong> ${total}</p>
        <p><strong>Average:</strong> ${average}</p>
        <p><strong>Grade:</strong> ${grade}</p>
    `;
});

// Timer
function startTimer() {
    setInterval(() => {
        let now = new Date();
        document.getElementById("timer").textContent = now.toLocaleTimeString();
    }, 1000);
}
startTimer();

// Todo List using Closure
function todoList() {
    let tasks = [];

    function addTask() {
        let task = document.getElementById("taskInput").value;
        if (task) {
            tasks.push(task);
            document.getElementById("taskInput").value = "";
            alert("Task added!");
        }
        else {
            alert("Please enter a task!");
        }
    }

    function removeTask() {
        let task = document.getElementById("taskInput").value;
        let index = tasks.indexOf(task);
        if (index > -1) {
            tasks.splice(index, 1);
            alert("Task removed!");
        } else {
            alert("Task not found!");
        }
        document.getElementById("taskInput").value = "";
    }

    function displayTasks() {
        let list = document.getElementById("taskList");
        list.innerHTML = "";
        tasks.forEach(t => {
            let li = document.createElement("li");
            li.textContent = t;
            list.appendChild(li);
        });
    }

    return { addTask, removeTask, displayTasks };
}
let todo = todoList();

// Calculator using Arrow Function
const calculate = () => {
    let num1 = parseFloat(document.getElementById("num1").value);
    let num2 = parseFloat(document.getElementById("num2").value);
    let op = document.getElementById("operation").value;
    let result;

    if (isNaN(num1) || isNaN(num2)) {
        alert("Enter valid numbers");
        return;
    }

    if (op === "+") result = (a, b) => a + b;
    else if (op === "-") result = (a, b) => a - b;
    else if (op === "*") result = (a, b) => a * b;

    document.getElementById("result").textContent = result(num1, num2);
};