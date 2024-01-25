const search = () => {
    let query = $("#search-input").val();
    console.log(query);

    if (query==='') {
        $(".search-result").hide();
    } else {
        console.log(query);

        //sending request to server
        let url = `http://localhost:8080/search/${query}`;
        fetch(url)
            .then((response) => {
                return response.json();
            })
            .then((data) => {
                console.log(data);
                let text = `<div class="list-group">`;
                data.forEach((note) => {
                    text += `<a href="/user/view-note/${note.id}" class="list-group-item list-group-item-action">${note.title}</a>`
                })
                text += `</div>`;
                $(".search-result").html(text);
                $(".search-result").show();
            });
    }
}