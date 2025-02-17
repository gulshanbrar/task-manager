// Plugin per disegnare il testo al centro del donut con Chart.js
const centerTextPlugin = {
  id: 'centerTextPlugin',
  afterDraw(chart, args, options) {
    if (options.display !== false) {
      const { ctx, width, height } = chart;
      ctx.save();
      ctx.font = options.font || 'bold 20px sans-serif';
      ctx.fillStyle = options.color || '#000';
      ctx.textAlign = 'center';
      ctx.textBaseline = 'middle';
      const text = options.text || '';
      ctx.fillText(text, width / 2, height / 2);
      ctx.restore();
    }
  }
};

// Registrazione del plugin
Chart.register(centerTextPlugin);

document.addEventListener("DOMContentLoaded", () => {
  // === Dati utente e aggiornamento visualizzazione ===
  const user = {
    name: "Gulshan",
    email: "Brar123@gmail.com",
    profileImg: "perfil.png",
    bio: "Scrivi qui la tua biografia."
  };

  const sidebarImg = document.getElementById("sidebar-profile-img");
  const sidebarName = document.getElementById("sidebar-user-name");
  const sidebarEmail = document.getElementById("sidebar-user-email");

  const profileImgElem = document.getElementById("profile-img");
  const profileNameInput = document.getElementById("profile-name");
  const profileEmailInput = document.getElementById("profile-email");
  const profileBioInput = document.getElementById("profile-bio");
  const profileSummary = document.getElementById("profile-summary");
  const profileForm = document.getElementById("profile-form");

  const updateUserDisplay = () => {
    sidebarImg.src = user.profileImg;
    sidebarName.textContent = user.name;
    sidebarEmail.textContent = user.email;
    
    profileImgElem.src = user.profileImg;
    profileNameInput.value = user.name;
    profileEmailInput.value = user.email;
    profileBioInput.value = user.bio;
    profileSummary.textContent = `Nome: ${user.name} | Email: ${user.email} | Bio: ${user.bio}`;
  };

  updateUserDisplay();

  profileForm.addEventListener("submit", (e) => {
    e.preventDefault();
    user.name = profileNameInput.value.trim();
    user.email = profileEmailInput.value.trim();
    user.bio = profileBioInput.value.trim();
    updateUserDisplay();
  });

  // === Toggle Sidebar e Navigazione ===
  const headerToggle = document.getElementById("header-toggle");
  const sidebar = document.getElementById("sidebar");
  const header = document.getElementById("header");
  const main = document.getElementById("main");

  headerToggle.addEventListener("click", () => {
    sidebar.classList.toggle("collapsed");
    if (sidebar.classList.contains("collapsed")) {
      header.classList.add("collapsed-pd");
      main.classList.add("collapsed-pd");
      header.classList.remove("left-pd");
      main.classList.remove("left-pd");
    } else {
      header.classList.remove("collapsed-pd");
      main.classList.remove("collapsed-pd");
      header.classList.add("left-pd");
      main.classList.add("left-pd");
    }
  });

  const navItems = document.querySelectorAll(".sidebar__link[data-section]");
  const showSection = (sectionName) => {
    document.querySelectorAll(".section").forEach((sec) => sec.style.display = "none");
    const targetSection = document.getElementById("section-" + sectionName);
    if (targetSection) {
      targetSection.style.display = "block";
    }
    navItems.forEach((el) => {
      if (el.getAttribute("data-section") === sectionName) {
        el.classList.add("active-link");
      } else {
        el.classList.remove("active-link");
      }
    });
  };

  navItems.forEach((item) => {
    item.addEventListener("click", (e) => {
      e.preventDefault();
      const sectionName = item.getAttribute("data-section");
      showSection(sectionName);
      history.pushState({section: sectionName}, '', `?section=${sectionName}`);
    });
  });

  window.addEventListener("popstate", (e) => {
    const sectionName = (e.state && e.state.section) ? e.state.section : "dashboard";
    showSection(sectionName);
  });

  const params = new URLSearchParams(window.location.search);
  const initialSection = params.get("section") || "dashboard";
  showSection(initialSection);

  // === Tema scuro/chiaro ===
  const themeButton = document.getElementById("theme-button");
  const darkTheme = "dark-theme";
  const savedTheme = localStorage.getItem("selected-theme");
  if (savedTheme) {
    document.body.classList[savedTheme === "dark" ? "add" : "remove"](darkTheme);
  }
  const updateThemeIcon = () => {
    const icon = themeButton.querySelector("i");
    if (document.body.classList.contains(darkTheme)) {
      icon.classList.remove("fa-moon");
      icon.classList.add("fa-sun");
    } else {
      icon.classList.remove("fa-sun");
      icon.classList.add("fa-moon");
    }
  };
  updateThemeIcon();
  themeButton.addEventListener("click", () => {
    document.body.classList.toggle(darkTheme);
    localStorage.setItem("selected-theme", document.body.classList.contains(darkTheme) ? "dark" : "light");
    updateThemeIcon();
  });

  // === Gestione delle Task e dei To Do ===
  let tasks = [];
  let editingTaskIndex = null;

  function loadTasks() {
    fetch("/api/tasks")
      .then(response => {
        if (!response.ok) {
          throw new Error("Errore nel caricamento delle task");
        }
        return response.json();
      })
      .then(data => {
        tasks = data;
        renderTasks();
        updateTaskStatusDonuts();
        updateDueTasks();
      })
      .catch(error => console.error("Errore nel caricamento delle task:", error));
  }

  // === Funzioni per i To Do ===
  let toDoItems = [];

  function loadToDos() {
    fetch("/api/todos")
      .then(response => {
        if (!response.ok) {
          throw new Error("Errore nel caricamento dei To Do");
        }
        return response.json();
      })
      .then(data => {
        toDoItems = data;
        renderToDos();
      })
      .catch(error => console.error("Errore nel caricamento dei To Do:", error));
  }

  function renderToDos() {
    const toDoListElem = document.getElementById("todo-list");
    toDoListElem.innerHTML = "";
    toDoItems.forEach(item => {
      const li = document.createElement("li");
      li.innerHTML = `
        <span class="todo-text">${item.testoTodolist}</span>
        <div class="todo-actions">
          <i class="ri-check-line complete-todo" title="Completa"></i>
          <i class="ri-pencil-line edit-todo" title="Modifica"></i>
          <i class="ri-close-line delete-btn" title="Elimina"></i>
        </div>
      `;
      li.querySelector(".delete-btn").addEventListener("click", () => {
        deleteToDoItem(item.id);
      });
      toDoListElem.appendChild(li);
    });
  }

  function createToDoItem(text) {
    const toDoData = {
      utenteId: 1,
      testoTodolist: text,
      completato: false
    };
    fetch("/api/todos", {
      method: "POST",
      headers: {
        "Content-Type": "application/json"
      },
      body: JSON.stringify(toDoData)
    })
      .then(response => {
        if (!response.ok) {
          throw new Error("Errore nella creazione del To Do");
        }
        return response.json();
      })
      .then(newItem => {
        loadToDos();
      })
      .catch(error => console.error("Errore:", error));
  }

  function deleteToDoItem(id) {
    fetch(`/api/todos/${id}`, {
      method: "DELETE"
    })
      .then(response => {
        if (!response.ok) {
          throw new Error("Errore nell'eliminazione del To Do");
        }
        return response.text();
      })
      .then(message => {
        console.log(message);
        loadToDos();
      })
      .catch(error => console.error("Errore:", error));
  }

  const addTodoBtn = document.getElementById("add-todo");
  const todoInput = document.getElementById("todo-input");

  addTodoBtn.addEventListener("click", () => {
    const text = todoInput.value.trim();
    if (text) {
      createToDoItem(text);
      todoInput.value = "";
    }
  });

  todoInput.addEventListener("keypress", (e) => {
    if (e.key === "Enter") {
      const text = todoInput.value.trim();
      if (text) {
        createToDoItem(text);
        todoInput.value = "";
      }
    }
  });

  // === Task Manager (gestione delle task) ===
  const taskForm = document.getElementById("task-form");
  const taskListElem = document.getElementById("task-list");
  const toggleTaskFormBtn = document.getElementById("toggle-task-form");
  const taskFormContainer = document.querySelector(".task-form-container");

  toggleTaskFormBtn.addEventListener("click", () => {
    taskFormContainer.classList.toggle("expanded");
  });

  function createTask(taskData) {
    fetch("/api/tasks", {
      method: "POST",
      headers: {
        "Content-Type": "application/json"
      },
      body: JSON.stringify(taskData)
    })
      .then(response => {
        if (!response.ok) {
          throw new Error("Errore nella creazione della task");
        }
        return response.json();
      })
      .then(newTask => {
        loadTasks();
        taskForm.reset();
      })
      .catch(error => {
        console.error("Errore:", error);
      });
  }

  taskForm.addEventListener("submit", (e) => {
    e.preventDefault();
    const title = document.getElementById("task-title").value.trim();
    const description = document.getElementById("task-description").value.trim();
    const date = document.getElementById("task-date").value;
    const priorityValue = document.getElementById("task-priority").value;
    // Usa trim() sul valore dello stato per eliminare spazi extra
    const statusRaw = document.getElementById("task-status").value.trim();  
    const status = statusRaw.charAt(0).toUpperCase() + statusRaw.slice(1);
    
    if (!title || !description || !date || !priorityValue || !status) return;
    
    let priority;
    if (priorityValue === "low") {
      priority = "Bassa";
    } else if (priorityValue === "medium") {
      priority = "Media";
    } else if (priorityValue === "high") {
      priority = "Alta";
    } else {
      priority = priorityValue;
    }

    const taskData = {
      utenteId: 1,
      titolo: title,
      descrizione: description,
      dataTask: date,
      priorita: priority,
      stato: status
    };

    if (editingTaskIndex === null) {
      createTask(taskData);
    } else {
      alert("La modifica delle task non è ancora implementata.");
    }
  });

  const renderTasks = () => {
    taskListElem.innerHTML = "";
    tasks.forEach((task, index) => {
      // Usa trim() per assicurarti di eliminare spazi indesiderati
      const statusClass = task.stato.trim().replace(/\s+/g, "-").toLowerCase();
      console.log("Stato:", task.stato, "-> Classe:", statusClass);
      const li = document.createElement("li");
      li.innerHTML = `
        <div class="task-info">
          <h3>${task.titolo}</h3>
          <p>${task.descrizione}</p>
          <small>
            Data: ${task.dataTask} - Priorità: ${capitalize(task.priorita)} - Stato: <span class="task-status ${statusClass}">${capitalize(task.stato)}</span>
          </small>
        </div>
        <div class="task-actions">
          <i class="ri-pencil-line edit-task" data-index="${index}" title="Modifica"></i>
          <i class="ri-close-line delete-btn" data-index="${index}" title="Elimina"></i>
        </div>
      `;
      li.querySelector(".delete-btn").addEventListener("click", () => {
        fetch(`/api/tasks/${task.id}`, {
          method: "DELETE"
        })
          .then(response => {
            if (!response.ok) {
              throw new Error("Errore nell'eliminazione della task");
            }
            return response.text();
          })
          .then(message => {
            console.log(message);
            loadTasks();
          })
          .catch(error => console.error("Errore:", error));
      });
      li.querySelector(".edit-task").addEventListener("click", () => {
        editingTaskIndex = index;
        document.getElementById("task-title").value = task.titolo;
        document.getElementById("task-description").value = task.descrizione;
        document.getElementById("task-date").value = task.dataTask;
        if (task.priorita === "Bassa") {
          document.getElementById("task-priority").value = "low";
        } else if (task.priorita === "Media") {
          document.getElementById("task-priority").value = "medium";
        } else if (task.priorita === "Alta") {
          document.getElementById("task-priority").value = "high";
        }
        document.getElementById("task-status").value = task.stato;
        document.getElementById("task-form-btn-text").textContent = "Salva Modifica";
        if (!taskFormContainer.classList.contains("expanded")) {
          taskFormContainer.classList.add("expanded");
        }
      });
      taskListElem.appendChild(li);
    });
  };

  const capitalize = (s) => s.charAt(0).toUpperCase() + s.slice(1);

  // === Pomodoro Timer e relative funzioni ===
  let pomodoroModes = {
    pomodoro: 25 * 60,
    "short-break": 5 * 60,
    "long-break": 15 * 60,
  };

  let currentMode = "pomodoro";
  let timerRemaining = pomodoroModes[currentMode];
  let timerInterval = null;
  let pomodoroCount = 0;

  const minutesElem = document.getElementById("pomodoro-minutes");
  const secondsElem = document.getElementById("pomodoro-seconds");
  const cycleElem = document.getElementById("pomodoro-cycle");
  const startBtn = document.getElementById("pomodoro-start");
  const pauseBtn = document.getElementById("pomodoro-pause");
  const resetBtn = document.getElementById("pomodoro-reset");
  const modeButtons = document.querySelectorAll(".pomodoro-mode");
  const pomodoroTimerElem = document.querySelector(".pomodoro-timer");

  const updateTimerDisplay = () => {
    const minutes = Math.floor(timerRemaining / 60);
    const seconds = timerRemaining % 60;
    minutesElem.textContent = minutes < 10 ? "0" + minutes : minutes;
    secondsElem.textContent = seconds < 10 ? "0" + seconds : seconds;
    updatePomodoroProgress();
  };

  const updatePomodoroProgress = () => {
    const totalTime = pomodoroModes[currentMode];
    const degree = (timerRemaining / totalTime) * 360;
    pomodoroTimerElem.style.background = `conic-gradient(var(--first-color) 0deg, var(--first-color) ${degree}deg, transparent ${degree}deg, transparent 360deg)`;
  };

  const startTimer = () => {
    if (timerInterval) return;
    timerInterval = setInterval(() => {
      if (timerRemaining <= 0) {
        clearInterval(timerInterval);
        timerInterval = null;
        pomodoroCount++;
        cycleElem.textContent = `Cycle: ${pomodoroCount}/5`;
        if (pomodoroCount % 5 === 0) {
          switchMode("long-break");
        } else {
          switchMode("short-break");
        }
        return;
      }
      timerRemaining--;
      updateTimerDisplay();
    }, 1000);
  };

  const pauseTimer = () => {
    clearInterval(timerInterval);
    timerInterval = null;
  };

  const resetTimer = () => {
    pauseTimer();
    timerRemaining = pomodoroModes[currentMode];
    updateTimerDisplay();
  };

  const switchMode = (mode) => {
    currentMode = mode;
    timerRemaining = pomodoroModes[mode];
    updateTimerDisplay();
    modeButtons.forEach((btn) => btn.classList.remove("active"));
    document.querySelector(`.pomodoro-mode[data-mode="${mode}"]`).classList.add("active");
  };

  modeButtons.forEach((btn) => {
    btn.addEventListener("click", () => {
      pauseTimer();
      switchMode(btn.getAttribute("data-mode"));
    });
  });

  startBtn.addEventListener("click", startTimer);
  pauseBtn.addEventListener("click", pauseTimer);
  resetBtn.addEventListener("click", resetTimer);
  updateTimerDisplay();

  const settingsBtn = document.getElementById("pomodoro-settings-btn");
  const settingsMenu = document.getElementById("pomodoro-settings-menu");
  const settingsForm = document.getElementById("pomodoro-settings-form");

  settingsBtn.addEventListener("click", () => {
    settingsMenu.classList.toggle("active");
  });

  settingsForm.addEventListener("submit", (e) => {
    e.preventDefault();
    const pomodoroVal = parseInt(document.getElementById("pomodoro-duration").value, 10) * 60;
    const shortBreakVal = parseInt(document.getElementById("short-break-duration").value, 10) * 60;
    const longBreakVal = parseInt(document.getElementById("long-break-duration").value, 10) * 60;
    pomodoroModes.pomodoro = pomodoroVal;
    pomodoroModes["short-break"] = shortBreakVal;
    pomodoroModes["long-break"] = longBreakVal;
    timerRemaining = pomodoroModes[currentMode];
    updateTimerDisplay();
    settingsMenu.classList.remove("active");
  });

  // === Funzioni aggiuntive per la Dashboard ===

  // Visualizza le task in scadenza (entro i prossimi 7 giorni) nella dashboard  
  // Ho aggiunto una sezione "card-footer" per etichettare lo stato e renderlo più visibile
  function updateDueTasks() {
    const container = document.getElementById("due-tasks-cards");
    container.innerHTML = "";
    const today = new Date();
    // Filtra le task con data compresa tra oggi e 7 giorni dopo
    const dueTasks = tasks.filter(task => {
      const taskDate = new Date(task.dataTask);
      const diffTime = taskDate - today;
      const diffDays = diffTime / (1000 * 60 * 60 * 24);
      return diffDays >= 0 && diffDays < 7;
    });

    if (dueTasks.length === 0) {
      container.innerHTML = "<p>Nessuna task in scadenza questa settimana.</p>";
      return;
    }

    dueTasks.forEach(task => {
      const statusClass = task.stato.trim().replace(/\s+/g, "-").toLowerCase();
      const card = document.createElement("div");
      card.classList.add("task-card");
      card.innerHTML = `
        <h3>${task.titolo}</h3>
        <p>${task.descrizione}</p>
        <small>Scadenza: ${task.dataTask}</small>
        <div class="card-footer">
          Stato: <span class="task-status ${statusClass}">${capitalize(task.stato)}</span>
        </div>
      `;
      container.appendChild(card);
    });
  }

  // Aggiorna i grafici a ciambella (donut) in base allo stato delle task
  function updateTaskStatusDonuts() {
    const statusCounts = {
      completata: 0,
      "in corso": 0,
      "da iniziare": 0
    };

    tasks.forEach(task => {
      const stato = task.stato.toLowerCase();
      if (stato === "completata") {
        statusCounts.completata++;
      } else if (stato === "in corso") {
        statusCounts["in corso"]++;
      } else if (stato === "da iniziare") {
        statusCounts["da iniziare"]++;
      }
    });

    const createDonutChart = (canvasId, count, label, color) => {
      const ctx = document.getElementById(canvasId).getContext("2d");
      new Chart(ctx, {
        type: 'doughnut',
        data: {
          labels: [label, "Altro"],
          datasets: [{
            data: [count, tasks.length - count],
            backgroundColor: [color, '#e0e0e0'],
            borderWidth: 0
          }]
        },
        options: {
          cutout: '70%',
          plugins: {
            legend: { display: false },
            tooltip: { enabled: false },
            centerTextPlugin: {
              display: true,
              text: count.toString(),
              font: "bold 20px sans-serif",
              color: "#000"
            }
          }
        }
      });
    };

    createDonutChart("donut-completed", statusCounts.completata, "Completate", "#28a745");
    createDonutChart("donut-inprogress", statusCounts["in corso"], "In Corso", "#ffc107");
    createDonutChart("donut-notstarted", statusCounts["da iniziare"], "Da Iniziare", "#dc3545");
  }

  // Carica le task e i To Do al caricamento della pagina
  loadTasks();
  loadToDos();
});
