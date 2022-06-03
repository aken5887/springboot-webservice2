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