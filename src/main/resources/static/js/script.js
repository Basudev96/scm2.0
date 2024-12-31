console.log("Script Loaded...");

let currentTheme = getTheme();

//initial

document.addEventListener('DOMContentLoaded', () => {
    changetheme(); 
})



//TODO:
function changetheme() {
    //set to web page
    changePageTheme(currentTheme, currentTheme);

    //set the listener to change the theme button
    const changeThemeButton = document.querySelector('#theme_change_button');
    // changeThemeButton.querySelector("span").textContent =
    //     currentTheme == "light" ? "Dark" : "Light";
    // const oldTheme = currentTheme;
    changeThemeButton.addEventListener('click', (event) => {
        console.log("change theme button clicked..")
        let oldTheme = currentTheme;
        if (currentTheme == "dark") {
            // setTheme("light");
            currentTheme = "light";
        } else {
            currentTheme = "dark";
        }

        changePageTheme(currentTheme, oldTheme);

    });

}
//set theme to local storage
function setTheme(theme) {
    localStorage.setItem("theme", theme);
}
//get theme from localstorage
function getTheme() {
    let theme = localStorage.getItem("theme");
    return theme ? theme : "light";
}
//change current page theme
function changePageTheme(theme, oldTheme) {
    setTheme(currentTheme);
    //remove the current theme
    document.querySelector('html').classList.remove(oldTheme);
    //set the current theme
    document.querySelector('html').classList.add(theme);

    document.querySelector("#theme_change_button").querySelector("span").textContent =
        theme == "light" ? "Dark" : "Light";
}