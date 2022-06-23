let valid = function(msg){
    let _title = $('#title');
    let title = _title.val();
    let flag = false;

    if(title == '' || title == undefined){
        alert("제목은 필수 값입니다.");
        _title.focus();
        return flag;
    }

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
            content: $('#contentArea').val(),
            fileId : $('#fileId').val()
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
            content : $('#contentArea').val()
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

function fn_kakao(){
    window.location.href = '/oauth2/authorization/kakao';
}

function fn_trigger_file(){
    $("input[name=file]").click();
}

function fn_file_change(tag){
    if(!isEmpty(tag.value) && isValidFile(tag)){
        $("#fileList").remove();
        $("input[name=fileId]").val("");

        let fileList = document.createElement("div");
        let aTag = document.createElement("a");
        let spanTag = document.createElement("span");

        fileList.setAttribute("id", "fileList");
        fileList.setAttribute("style", "padding-top: 5px;");
        aTag.setAttribute("class", "tag_del");
        aTag.setAttribute("id", "tag_del");
        aTag.setAttribute("href", "javascript:fn_delete_file_list();")
        aTag.innerText = "삭 제";
        spanTag.innerText = tag.value;
        spanTag.setAttribute("id", "file_span");

        $(".filse_list")[0].append(fileList);
        $("#fileList").append(aTag).append(spanTag);
    }
}

function fn_delete_file_list(){
  console.log('fn_delete_file_list called');
  $("#fileList").remove();
  $("input[name=file]").val("");
  $("input[name=fileId]").val("");
}

function fn_upload_file(){
   const fileForm = $("#fileForm")[0];
   const formData = new FormData(fileForm);
   const uploadFile = $("#fileUpload").val();

   console.log('fileForm : '+fileForm);
   console.log('formData : '+formData);
   console.log('uploadFile : '+uploadFile);

   if(isEmpty(uploadFile)){
      alert("업로드할  파일을 등록하세요.");
      return;
   }

   $.ajax({
        method : 'POST',
        url : '/upload',
        enctype : 'multipart/form-data',
        processData : false,
        contentType : false,
        cache : false,
        timeout : 600000,
        data : formData
   }).done(function(res){
       alert('업로드에 성공하였습니다.');
       $("input[name=fileId]").val(res);
       $("#file_span").attr("onclick", "javascript:fn_download();");
       $("#file_span").css("cursor", "pointer");
   }).fail(function(res){
       alert(JSON.stringify(res));
   });
}

function fn_download(){
    const fileId = $("input[name=fileId]").val();
    if(!isEmpty(fileId)){
//        $.ajax({
//            method : 'GET',
//            url : '/download',
//            data : { resourcePath : fileId },
//            dataType : 'json',
//            contentType : 'application/x-www-form-urlencoded;charset=UTF-8'
//        }).fail(function(res){
//           alert(JSON.stringify(res));
//        });
        let aDownTag = document.createElement("a");
        aDownTag.setAttribute("href", "/download?resourcePath="+fileId);
        document.body.appendChild(aDownTag);
        aDownTag.click();
        document.body.removeChild(aDownTag);
    }
}

function isEmpty(value){
    if(value == '' || value == 'undefined' || value == null){
        return true;
    }
    return false;
}

function isValidFile(file){
    let fileVal = $(file).val();
    let ext = fileVal.split('.').pop().toLowerCase();
    const validExt = ['jpg', 'png', 'jpeg', 'zip', 'gif'];

    if($.inArray(ext, validExt) == -1){
        alert(validExt + ' 파일만 업로드 할 수 있습니다.');
        return false;
    }

    const maxFileSize = 20 * 1024 * 1024; // 20MB
    let fileSize = $(file)[0].files[0].size;

    if(fileSize > maxFileSize){
        alert("첨부파일 사이즈는 20MB 이내로 등록 가능합니다.");
        $(file).val("");
        return false;
    }

    return true;
}