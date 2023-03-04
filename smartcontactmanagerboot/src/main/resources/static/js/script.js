	const search= () => {
    console.log("searching");
    let query = document.getElementById("search-input").value;
    var x = document.getElementById("search-result");
    if (query=="" && x.style.display === "none") {
        x.style.display = "none";
    } else if(query!="") {
        console.log(query);
        let url = `http://localhost:8080/search/`+query;
        fetch(url).then((response) => {
            return response.json();
        })
        .then((data) =>{
            console.log(data);
            let text=`<div class='list-group'>`;
            data.forEach((contact) =>{
                text+=`<a href='/smartContactManager/user/show-contacts/details/${contact.id}' class='list-group-item list-group-action'> ${contact.name} </a>`;
            });
            text+=`</div>`;
            x.innerHTML= text;

            
        });
        x.style.display = "block";
    }
    else{
        x.style.display = "none";
    }
    //$(".search-input").val();
    // if(query==""){
    //     document.getElementsById("search-result").show;
    //     //$(".search-result").hide();
    // }
    // else{
    //     console.log(query);
    //     document.getElementsById("search-result").show;
    //     // $(".search-result").show();
    // }
};