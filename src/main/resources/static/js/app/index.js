let valid = function(msg){
    let flag = false;
    if(confirm(msg)){
        flag = true;
    }
    return flag;
}

let main = {
    init : function() {
        let _this = this;
        $('#btn-save').click(function(){
            if(valid("저장하시겠습니까?")){
                _this.save();
            }
        });
        $('#btn-update').click(function(){
            if(valid("수정하시겠습니까?")){
                _this.update();
            }
        });
        $('#btn-delete').click(function(){
            if(valid("삭제하시겠습니까?")){
                _this.delete();
            }
        });
        $('#btn-search').click(function(){
            $("#search-form").submit();
        });

        let selType = $("#selType").val();
        let selKeyword = $("#selKeyword").val();

        if(selType != '' && selType != undefined){
            $("#type").val(selType);
        }
        if(selKeyword != '' && selKeyword != undefined){
            $("#keyword").val(selKeyword);
        }
        let currentPNo = $("#currentPNo").val();
        $("#pageEle"+currentPNo).addClass("on");
    },
    save : function () {
         let data = {
            title : $('#title').val(),
            author: $('#author').val(),
            content: $('#content').val()
         };

         $.ajax({
            type : 'POST',
            url : '/api/v1/posts',
            dataType : 'json',
            contentType : 'application/json; charset=utf-8',
            data : JSON.stringify(data)
         }).done(function(){
            alert('글이 등록 되었습니다.');
            window.location.href = '/';
         }).fail(function(res){
            alert(JSON.stringify(res));
         });
    },
    update : function(){

        let id = $('#id').val();

        let data = {
            title : $('#title').val(),
            content : $('#content').val()
        };

        $.ajax({
            method : 'PUT',
            url : '/api/v1/posts/'+id,
            dataType : 'json',
            contentType : 'application/json; charset=utf-8',
            data : JSON.stringify(data)
        }).done(function(){
            alert('글이 수정되었습니다.');
            window.location.href = '/';
        }).fail(function(res){
            alert(JSON.stringify(res));
        });
    },
    delete : function(){
         let id = $('#id').val();

         $.ajax({
             method : 'DELETE',
             url : '/api/v1/posts/'+id,
             dataType : 'json',
             contentType : 'application/json; charset=uft-8'
         }).done(function(){
            alert('삭제 완료되었습니다.');
            window.location.href = '/';
         }).fail(function(res){
            alert(JSON.stringify(res));
         });
    }
};

main.init();

function fn_move_page(page, moveNo) {
     let movePageNo = page + moveNo;
     $("#page").val(movePageNo);
     $("#search-form").submit();
}

function fn_search(e){
    if(e.keyCode == 13){
        $("#search-form").submit();
    }
}

function fn_update_user_auth(){
    $.ajax({
        method : 'GET',
        url : '/api/v1/user/auth'
    }).done(function(){
        alert("정상적으로 변경되었습니다.\n다시 로그인 해주세요.");
        window.location.href = '/';
    }).fail(function(res){
        alert(JSON.stringify(res));
    });
}