const nav = document.querySelector('.right');
const bur = document.getElementById('burgerId');
bur.addEventListener('click', ()=>
{
    nav.classList.toggle('show');
    nav.classList.toggle('hide');
});
        