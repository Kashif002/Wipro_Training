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