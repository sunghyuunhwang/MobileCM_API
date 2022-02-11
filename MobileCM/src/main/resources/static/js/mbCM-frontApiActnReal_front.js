function stiResultStatus() {//시공팀별실적현황가져오기
let fdt = $('#nmldate1').val();
let tdt = $('#nmldate2').val();
$('#stiResultStatus_1').find('.ulLftlst._data').remove();
$('#stiResultStatus_2').find('.ulLftlst._data').remove();
     $.ajax({
       url: "/v1/api/tmserp/getStiPerformInformation",
       type: "GET",
       cache: false,
       dataType: "json",
       data: {
          fdt:fdt,
          tdt:tdt
       },
       success: function(list){
            $.each(list, function(idx, response) {
               var stiRStts_1 = "<ul class='ulLftlst _data'>";
                   stiRStts_1 += "<li class='w150px _cdata' data-stinm='"+response.sti_nm+"'>"+response.sti_nm+"<input type='hidden' value='"+response.sti_cd+"' class='_sti_cd'/></li>";
                   stiRStts_1 += "<li class='w120px'><span class='numTxt'></span><input type='text' name='' value='"+response.orm_amt+"' class='innmbr nmCmma _orm_amt'></li>";
                   stiRStts_1 += "<li class='w120px'><span class='numTxt'></span><input type='text' name='' value='"+response.constcst_sum+"' class='innmbr nmCmma _constcst_sum'></li>";
                   stiRStts_1 += "<li class='w85px tAlgnCntr _happycall_average'>"+response.happycall_average+"</li>";
                   stiRStts_1 += "<li class='w110px'>"+response.sigonghaja_average+"%("+response.sigonghaja_cnt+"건)</li>";
                   stiRStts_1 += "<li class='w110px tAlgnRght'>"+response.inconsistent_average+"%("+response.total_sigong_cnt+"건)</li>";
                   stiRStts_1 += "<li class='w95px tAlgnCntr'>"+response.service_cnt+"</li>";
                   stiRStts_1 += "<li class='w90px tAlgnRght'>"+response.claim_cnt+"</li>";
                   stiRStts_1 += "</ul>";
                $('#stiResultStatus_1').append(stiRStts_1);
            console.log(response);
            });
              ulLftlst();
               cmma();//콤마
               innmbr();//인풋값 스팬으로 넘기기

       },
       error: function (request, status, error){
            console.log("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
            alert('데이터를 불러올수 없습니다.');
       }
   });
   $.ajax({
    url: "/v1/api/tmserp/getStiDuedayInformation",
    type: "GET",
    cache: false,
    dataType: "json",
    data: {
        fdt:fdt,
        tdt:tdt
    },
    success: function(list){
          $.each(list, function(idx, response) {
             var stiRStts_2 = "<ul class='ulLftlst _inDpthClr _grn'>";
                 stiRStts_2 += "<li class='w150px _inDCr'>"+response.sti_nm+"<input type='hidden' value='"+response.sti_cd+"' class='_sti_cd'/></li>";
                 stiRStts_2 += "<li class='w80px tAlgnCntr _inDCr'>"+response.sti_qtycapa+"</li>";
                 stiRStts_2 += "<li class='w120px _inDCr'><span class='numTxt'></span><input type='text' name='' value='"+response.sti_amtcapa+"' class='innmbr nmCmma _orm_amt'></li>";
                 stiRStts_2 += "<li class='w80px tAlgnCntr _inDCr'>"+response.current_dueday+"</li>";
                 stiRStts_2 += "<li class='w110px'><span class='numTxt'></span><input type='text' name='' value='"+response.sti_currentamt+"' class='innmbr nmCmma _orm_amt'></li>";
                 stiRStts_2 += "<li class='w110px'><span class='numTxt'></span><input type='text' name='' value='"+response.avg_amt+"' class='innmbr nmCmma _orm_amt'></li>";
                 stiRStts_2 += "<li class='w95px tAlgnCntr'><span class='numTxt'></span><input type='text' name='' value='"+response.sum_currentqty+"' class='innmbr nmCmma _orm_amt'></li>";
                 stiRStts_2 += "<li class='w85px tAlgnCntr'><span class='numTxt'></span><input type='text' name='' value='"+response.avg_cnt+"' class='innmbr nmCmma _orm_amt'></li>";
                 stiRStts_2 += "<li class='w85px tAlgnCntr'>"+response.max_dueday+"</li>";
                 stiRStts_2 += "<li class='w85px tAlgnRght'>"+response.avg_dueday+"</li>";
                 stiRStts_2 += "</ul>";
              $('#stiResultStatus_2').append(stiRStts_2);
          console.log(response);
          });
             ulLftlst();
             valundefined();
             cmma();//콤마
             innmbr();//인풋값 스팬으로 넘기기

    },
    error: function (request, status, error){
          console.log("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
          alert('데이터를 불러올수 없습니다.');
    }
   });
}

function delstimmbrChckPop(){//시공팀원정보삭제확인
     $(document).on("click",".delstimmbrChck",function(){
         $('.delstimmbrChckPop').addClass('opn');
         var stm_nm = $(".stiMmbrMdfPop._Mdf").find('._stm_nm').val();
         $('.alert_stm_nm').text(stm_nm);
    });
}
function delstimmbrinf(data){//시공팀원정보삭제
    $('.delstimmbrChckPop').removeClass('opn');
     let mmbrpop = $(".stiMmbrMdfPop._Mdf");
     let stm_no = mmbrpop.find('._stm_no').val();
     let com_scd = mmbrpop.find('._com_scd').val();
     let sti_cd = mmbrpop.find('._sti_cd').val();

     $.ajax({
            url: "/v1/api/tmserp/tmserp_insertstimemberinfo",
            type: "DELETE",
            cache: false,
            dataType: "json",
            contentType: 'application/json',
            data:{
            stm_no:stm_no,
            com_scd:com_scd,
            sti_cd:sti_cd
            },
            success : function(data){
                 alert("팀원 정보가 삭제되었습니다.");
                 mmbrpop.removeClass('opn _insrt');
                 window.location.reload();

          }
     });
}
function insrtstimmbrinf() {//시공팀원정보등록
     var mmbrpop = $(".stiMmbrMdfPop._insrt");
     var astm_addr = mmbrpop.find('.address_stm_addr').val();
     var dstm_addr = mmbrpop.find('#detailAddress').val();
     var stm_addr = mmbrpop.find('._stm_addr');
     stm_addr.val(astm_addr+dstm_addr);
     var stm_addrinf = mmbrpop.find('._stm_addr').val();
     var stm_nm = mmbrpop.find('._stm_nm').val();
     var stm_no = mmbrpop.find('._stm_no').val();
     var com_scd = mmbrpop.find('._com_scd').val();
     var sti_cd = mmbrpop.find('._sti_cd').val();
     var stm_hp = mmbrpop.find('._stm_hp').val();
     var com_pos = mmbrpop.find('._com_pos').val();
     var stm_zip = mmbrpop.find('._stm_zip').val();
     var car_no = mmbrpop.find('._car_no').val();
     var stm_jdt = mmbrpop.find('._stm_jdt').val();

     var stimember = {
          stm_addr:stm_addrinf,
     stm_nm:stm_nm,
     stm_no:stm_no,
     com_scd:com_scd,
     sti_cd:sti_cd,
     stm_hp:stm_hp,
     com_pos:com_pos,
     stm_zip:stm_zip,
     car_no:car_no,
     stm_jdt:stm_jdt
     }
     if(stm_nm.length < 1 || (stm_jdt.length < 1 || isNaN(stm_jdt))){
          alert('팀원 이름/입사일은 필수입력 사항입니다. 입사일은 숫자 8자리로 입력해주세요. (예 : 20200909)');
          if(stm_nm.length < 1){
               mmbrpop.find('._stm_nm').addClass('wrng');
          }
          if(stm_jdt.length < 1 || isNaN(stm_jdt)){
              mmbrpop.find('._stm_jdt').addClass('wrng');
          }
     }else{
           mmbrpop.find('._stm_nm').removeClass('wrng');
           mmbrpop.find('._stm_jdt').removeClass('wrng');
          $.ajax({
                 url: "/v1/api/tmserp/tmserp_insertstimemberinfo",
                 type: "POST",
                 cache: false,
                 dataType: "json",
                 contentType: 'application/json',
                 data:{
                      stimember
                 },
                 data : JSON.stringify(stimember),
                 success : function(stimember){
                      alert("팀원 정보가 등록되었습니다.");
                      mmbrpop.removeClass('opn _insrt');
                      window.location.reload();

               }
          });
     }

}

function updtstimmbrinf() {//시공팀원정보수정
     let mmbrpop = $(".stiMmbrMdfPop._Mdf");
     let astm_addr = mmbrpop.find('.address_stm_addr').val();
     let dstm_addr = mmbrpop.find('#detailAddress').val();
     var stm_addr = mmbrpop.find('._stm_addr');
     stm_addr.val(astm_addr+dstm_addr);
     let stm_addrinf = mmbrpop.find('._stm_addr').val();
     let stm_nm = mmbrpop.find('._stm_nm').val();
     let stm_no = mmbrpop.find('._stm_no').val();
     let com_scd = mmbrpop.find('._com_scd').val();
     let sti_cd = mmbrpop.find('._sti_cd').val();
     let stm_hp = mmbrpop.find('._stm_hp').val();
     let com_pos = mmbrpop.find('._com_pos').val();
     let stm_zip = mmbrpop.find('._stm_zip').val();
     let car_no = mmbrpop.find('._car_no').val();
     let stm_jdt = mmbrpop.find('._stm_jdt').val();

     var stimember = {
         stm_addr:stm_addrinf,
         stm_nm:stm_nm,
         stm_no:stm_no,
         com_scd:com_scd,
         sti_cd:sti_cd,
         stm_hp:stm_hp,
         com_pos:com_pos,
         stm_zip:stm_zip,
         car_no:car_no,
         stm_jdt:stm_jdt
     }

     if(stm_nm.length < 1 || (stm_jdt.length < 1 || isNaN(stm_jdt))){
          alert('팀원 이름/입사일은 필수입력 사항입니다. 입사일은 숫자 8자리로 입력해주세요. (예 : 20200909)');
          if(stm_nm.length < 1){
               mmbrpop.find('._stm_nm').addClass('wrng');
          }
          if(stm_jdt.length < 1 || isNaN(stm_jdt)){
              mmbrpop.find('._stm_jdt').addClass('wrng');
          }
     }else{
           mmbrpop.find('._stm_nm').removeClass('wrng');
           mmbrpop.find('._stm_jdt').removeClass('wrng');
       $.ajax({
              url: "/v1/api/tmserp/tmserp_updatestimemberinfo",
              type: "PUT",
              cache: false,
              dataType: "json",
              contentType: 'application/json',
              data:{
                   stimember
              },
              data : JSON.stringify(stimember),
              success : function(stimember){
                   alert("팀원 정보가 수정되었습니다.");
                   mmbrpop.removeClass('opn _Mdf');
                   window.location.reload();

            }
       });
     }

}

function getComcdList() {//시공팀직책리스트
     $.ajax({
       url: "/v1/api/tmserp/getComcdList",
       type: "GET",
       cache: false,
       dataType: "json",
       data: {
            cdx_cd:'C12'
       },
       success: function(list){
            $.each(list, function(idx, response) {
               var getCmcdLst = "<li class='slct'><span class='slctTxt _ccd_name' data-value='"+response.ccd_cd+"'>"+response.ccd_name+"</span></li>";
                $('#getComcdList').append(getCmcdLst);
            console.log(response);
            });

       },
       error: function (request, status, error){
            console.log("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
            alert('데이터를 불러올수 없습니다.');
       }
   });


}
function getStiMemberDetailInfo(e) {//시공팀원정보상세조회
     $('#getStiMemberDetailInfo > li').remove();
     let sti_cd = $(e).find('._sti_cd').text();
     let com_scd = $(e).find('._com_scd').val();
     let tmName = $(e).find('._com_scd').parents('li').text();
     let total_qty = $(e).find('._total_qty');
     $.ajax({
       url: "/v1/api/tmserp/getStiMemberDetailInfo",
       type: "GET",
       cache: false,
       dataType: "json",
       data: {
            sti_cd: sti_cd,
            com_scd:com_scd
       },
       success: function(list){
          if(total_qty.text() == '0'){
              $('#getStiMemberDetailInfo').append("<li class='nonData'>팀원정보가 없습니다.</li>");
          }else{
            $.each(list, function(idx, response) {
               var getStiMmbrDtl = "<li class='_stiMmbrDtil'>";
                   getStiMmbrDtl += "<dl class='inInfoDl wCal390px'><dt class='ttl'>팀원성명</dt><dd class='inf bld _stm_nm'>"+response.stm_nm+"<input type='hidden' value='"+response.stm_no+"' class='_stm_no'/><input type='hidden' value='"+response.sti_cd+"' class='_sti_cd'/></dd></dl>";
                   getStiMmbrDtl += "<dl class='inInfoDl w215px'><dt class='ttl'>전화번호</dt><dd class='inf bld _stm_hp'>"+response.stm_hp+"</dd></dl>";
                   getStiMmbrDtl += "<dl class='inInfoDl w175px'><dt class='ttl'>직책</dt><dd class='inf _com_pos'>"+response.com_pos+"</dd></dl>";
                   getStiMmbrDtl += "<dl class='inInfoDl wCal360px'><dt class='ttl'>우편번호</dt><dd class='inf _stm_zip'>"+response.stm_zip+"</dd></dl>";
                   getStiMmbrDtl += "<dl class='inInfoDl w185px'><dt class='ttl'>차량번호</dt><dd class='inf _car_no'>"+response.car_no+"</dd></dl>";
                   getStiMmbrDtl += "<dl class='inInfoDl w175px'><dt class='ttl'>입사일</dt><dd class='inf _stm_jdt'>"+response.stm_jdt+"</dd></dl>";
                   getStiMmbrDtl += "<dl class='inInfoDl w100p br0'><dt class='ttl'>주소</dt><dd class='inf _stm_addr'>"+response.stm_addr+"</dd></dl>";
                   getStiMmbrDtl += "</li>";
                    $('#getStiMemberDetailInfo').append(getStiMmbrDtl);
                inInfoDlwh();
                console.log(response);
            });
          }
          $('.stiMmbrInsrtBtn').addClass('on');
          $('.stiMmbrInsrtBtn').text(tmName + '팀의 팀원등록');

       },
       error: function (request, status, error){
            console.log("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
            alert('데이터를 불러올수 없습니다.');
       }
   });


}
function getStiMemberInfo() {//시공팀원정보현황조회
     $.ajax({
       url: "/v1/api/tmserp/getStiMemberInfo",
       type: "GET",
       cache: false,
       dataType: "json",
       data: {
       },
       success: function(list){
            $.each(list, function(idx, response) {
               var getStiMmbrInf = "<ul class='ulLftlst' onclick='getStiMemberDetailInfo(this);'>";
                   getStiMmbrInf += "<li class='w130px'>"+response.sti_nm+"<input type='hidden' value='"+response.com_scd+"' class='_com_scd'/></li>";
                   getStiMmbrInf += "<li class='w130px _sti_cd'>"+response.sti_cd+"</li>";
                   getStiMmbrInf += "<li class='w80px tAlgnCntr _total_qty'>"+response.total_qty+"</li>";
                   getStiMmbrInf += "<li class='w50px tAlgnCntr _car_qty'>"+response.car_qty+"</li>";
                   getStiMmbrInf += "</ul>";
                $('#getStiMemberInfo').append(getStiMmbrInf);
                ulLftlst();
                stiMmbrInfAllnmber();
            console.log(response);
            });

       },
       error: function (request, status, error){
            console.log("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
            alert('데이터를 불러올수 없습니다.');
       }
   });


}
function saveOpinion() {//하자내역상새조회
     $(document).on("click",".saveOpnnBtn",function(){
           var rpt_no = $('.getDfctInfLst.on').find('_rpt_no').text();
           var rpt_seq = $('.getDfctInfLst.on').find('_rpt_seq').text();
           var opinion = $('#opinion').val();
         $.ajax({
               url: "/v1/api/tmserp/saveOpinion",
               type: "POST",
               cache: false,
               dataType: "json",
               contentType: 'application/json',
               data:{
                    rpt_no: rpt_no,
                    rpt_seq: rpt_seq,
                    opinion: opinion
               },
               data : JSON.stringify(data),
               success : function(data){   //파일 주고받기가 성공했을 경우. data 변수 안에 값을 담아온다.
                    alert("이의제기 내용이 저장되었습니다.");

              }
         });
     });


}
function getDfctDetail() {//하자내역상새조회
     $.ajax({
       url: "/v1/api/tmserp/getDefectDetail",
       type: "GET",
       cache: false,
       dataType: "json",
       data: {
                rpt_no: 'I202109290263',
                rpt_seq: '01'
       },
       success: function(list){
            $.each(list, function(idx, response) {
               var getDfctDetail = "<ul class='ulLftlst'>";
                   getDfctDetail += "<li class='w150px'>"+response.itm_cd+"</li>";
                   getDfctDetail += "<li class='w200px'>"+response.bmt_item+"</li>";
                   getDfctDetail += "<li class='wCal680px tAlgnCntr'>"+response.col_cd+"</li>";
                   getDfctDetail += "<li class='w100px tAlgnCntr'>"+response.ast_actqty+"</li>";
                   getDfctDetail += "<li class='w80px  tAlgnCntr'><button class='inFileBtn'>"+response.file_yn+"</button></li>";
                   getDfctDetail += "</ul>";
                $('#getDfctDetail').append(getDfctDetail);
                inFileBtnPop();
                ulLftlst();
            console.log(response);
            });

       },
       error: function (request, status, error){
            console.log("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
            alert('데이터를 불러올수 없습니다.');
       }
   });


}
function getDfctInf() {//하자내역조회
     $.ajax({
       url: "/v1/api/tmserp/getDefectInfoList",
       type: "GET",
       cache: false,
       dataType: "json",
       data: {
                from_dt: '20210910',
                to_dt: '20211020',
                ctm_nm: ''
       },
       success: function(list){
            $.each(list, function(idx, response) {
               var getDfctInf = "<ul class='ulLftlst getDfctInfLst'>";
                   getDfctInf += "<li class='w110px'>"+response.rpt_enddt+"</li>";
                   getDfctInf += "<li class='w150px'><a class='lnkLn _rpt_no' onclick='getDfctDetail();'>"+response.rpt_no+"</a></li>";
                   getDfctInf += "<li class='w80px tAlgnCntr _rpt_seq'>"+response.rpt_seq+"</li>";
                   getDfctInf += "<li class='w300px'>"+response.vnd_snm+"</li>";
                   getDfctInf += "<li class='w100px tAlgnLft'>"+response.ctm_nm+"</li>";
                   getDfctInf += "<li class='w150px'>"+response.sti_nm+"<input type='hidden' value='"+response.sti_cd+"' /></li>";
                   getDfctInf += "<li class='w110px'>"+response.plm_cdt+"</li>";
                   getDfctInf += "<li class='w150px'>"+response.rpt_rst_acttm_nm+"<input type='hidden' value='"+response.rpt_rst_acttm+"' /></li>";
                   getDfctInf += "<li class='w100px'>"+response.rpt_usrnm+"</li>";
                   getDfctInf += "<li class='w80px tAlgnCntr'>"+response.rtnsec+"</li>";
                   getDfctInf += "<li class='w80px tAlgnCntr'><button class='inFileBtn'>"+response.file_yn+"</button></li>";
                   getDfctInf += "<li class='dsplyNon _rpt_astdesc'>"+response.rpt_astdesc+"</li>";
                   getDfctInf += "<li class='dsplyNon _rpt_desc'>"+response.rpt_desc+"</li>";
                   getDfctInf += "<li class='dsplyNon _opinion'>"+response.opinion+"</li>";
                   getDfctInf += "</ul>";
                $('#getDfctInf').append(getDfctInf);
                inFileBtnPop();
                $('.getDfctInfLst').click(function() {
                   $('.ulLftlst').removeClass('on');
                   var rpt_astdesc = $(this).find('._rpt_astdesc').text();
                   var rpt_desc =  $(this).find('._rpt_desc').text();
                   var opinion =  $(this).find('._opinion').text();
                     if(rpt_astdesc.length > 0){
                      $('#rpt_astdesc').text(rpt_astdesc);
                     }else{
                       $('#rpt_astdesc').text('전달 사항이 없습니다.');
                     }
                     if(rpt_desc.length > 0){
                        $('#rpt_desc').text(rpt_desc);
                     }else{
                        $('#rpt_desc').text('요구 내역이 없습니다.');
                     }
                     if(opinion.length > 0){
                        $('#opinion').text(opinion);
                     }else{
                        $('#opinion').text('이의 제기내용이 없습니다.');
                     }
         	           $(this).addClass('on');
                });
            console.log(response);
            });

       },
       error: function (request, status, error){
            console.log("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
            alert('데이터를 불러올수 없습니다.');
       }
   });


}
function getMglSubInf() {//상세상세미결현황요약정보
     $('#getMglSubInf').find('.ulLftlst._data').remove();
     $.ajax({
       url: "/v1/api/tmserp/getMigyeoDetaillnfo",
       type: "GET",
       cache: false,
       dataType: "json",
       data: {
                plm_no: 'I202110202227'
       },
       success: function(list){
            $.each(list, function(idx, response) {
               var getMglSubInf = "<ul class='ulLftlst _data'>";
                   getMglSubInf += "<li class='w60px _lstNum'></li>";
                   getMglSubInf += "<li class='w150px'>"+response.com_pldsec+"</li>";
                   getMglSubInf += "<li class='w150px'>"+response.itm_cd+"</li>";
                   getMglSubInf += "<li class='w150px'>"+response.col_cd+"</li>";
                   getMglSubInf += "<li class='w250px'><!--단품명변수--></li>";
                   getMglSubInf += "<li class='w100px tAlgnCntr'><span class='numTxt'></span><input type='text' name='' value='"+response.pld_eqty+"' class='innmbr nmCmma w100p'></li>";
                   getMglSubInf += "<li class='w150px tAlgnRght'><span class='numTxt'></span><input type='text' name='' value='"+response.pld_famt+"' class='innmbr nmCmma w100p'></li>";
                   getMglSubInf += "<li class='w150px'>"+response.com_updsec+"</li>";
                   getMglSubInf += "<li class='w300px'>"+response.pld_rmk+"</li>";
                   getMglSubInf += "</ul>";
                $('#getMglSubInf').append(getMglSubInf);
                   cmma();//콤마
                   innmbr();//인풋값 스팬으로 넘기기
                  ulLftlst();
            console.log(response);
            });

       },
       error: function (request, status, error){
            console.log("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
            alert('데이터를 불러올수 없습니다.');
       }
   });


}
function getMigyeolInfo() {//상세미결현황요약정보
     $('.migyeolInfo').addClass('opn');
     $.ajax({
       url: "/v1/api/tmserp/getMigyeolnfo",
       type: "GET",
       cache: false,
       dataType: "json",
       data: {
                from_dt: '20210910',
                to_dt: '20211020',
                com_scd:'C16YA',
                ksti_cd:'YA601',
                sti_cd: 'YA601'
       },
       success: function(list){
            $.each(list, function(idx, response) {
               var mglRprtInf = "<ul class='ulLftlst _inmigyeolInf'>";
                   mglRprtInf += "<li class='w150px'><a class='lnk' onclick='getMglSubInf()'>"+response.sti_nm+"</a></li>";
                   mglRprtInf += "<li class='w100px'>"+response.com_ssec+"<input type='hidden' name='plm_no' value='"+response.plm_no+"' /></li>";
                   mglRprtInf += "<li class='w110px'>"+response.rem_dt+"</li>";
                   mglRprtInf += "<li class='w100px'>"+response.com_brand+"</li>";
                   mglRprtInf += "<li class='w150px'>"+response.agt_nm+"</li>";
                   mglRprtInf += "<li class='w200px'>"+response.orm_nm+"</li>";
                   mglRprtInf += "<li class='w150px'>"+response.orm_no+"</li>";
                   mglRprtInf += "<li class='w80px tAlgnCntr _unpsecTxt'>"+response.unpsec_r_yn+"</li>";
                   mglRprtInf += "<li class='w80px tAlgnCntr _unpsecTxt'>"+response.unpsec_a_yn+"</li>";
                   mglRprtInf += "<li class='w80px tAlgnCntr _unpsecTxt'>"+response.unpsec_e_yn+"</li>";
                   mglRprtInf += "<li class='w100px tAlgnCntr _unpsecTxt'>"+response.unpsec_c_yn+"</li>";
                   mglRprtInf += "<li class='w80px tAlgnCntr'><button class='inFileBtn'>"+response.file_yn+"</button></li>";
                   mglRprtInf += "<li class='w300px'>"+response.mob_rmk+"</li>";
                   mglRprtInf += "</ul>";
                $('#getMigyeolInf').append(mglRprtInf);
                migyeolinfStl() ;//미경상세화면변경
                inFileBtnPop();
                ulLftlst();
            console.log(response);
            });

       },
       error: function (request, status, error){
            console.log("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
            alert('데이터를 불러올수 없습니다.');
       }
   });


}
function getMigyeolReportInfo() {//미결현황요약정보
     $.ajax({
       url: "/v1/api/tmserp/getMigyeolReportInfo",
       type: "GET",
       cache: false,
       dataType: "json",
       data: {
                from_dt: '20210910',
                to_dt:'20211020',
                com_scd:'C16YA',
                ksti_cd:'YA601'
       },
       success: function(list){
            $.each(list, function(idx, response) {
                var tot_cnt = response.tot_cnt;
                var comp_cnt = response.comp_cnt;
                var migyeol_cnt = response.migyeol_cnt
                var comp_per = response.comp_per;
                var migyeol_per = response.migyeol_per;
                var com_unpsec_r = response.com_unpsec_r;
                var com_unpsec_a= response.com_unpsec_a;
                var com_unpsec_e = response.com_unpsec_e;
                var com_unpsec_c = response.com_unpsec_c;
                var comp_unpsec_r_per = response.com_unpsec_r_per;
                var comp_unpsec_a_per= response.com_unpsec_a_per;
                var comp_unpsec_e_per = response.com_unpsec_e_per;
                var comp_unpsec_c_per = response.com_unpsec_c_per;
               $('.ttl._tot').find('.numCnt').text(tot_cnt);
               $('.ttl._migyeol').find('.numCnt').text(migyeol_cnt);
               $('.bar._comp').find('.numCnt').text(comp_cnt);
               $('.bar._migyeol').find('.numCnt').text(migyeol_cnt);
               $('.bar._comp').find('.numPer').text(comp_per);
               $('.bar._migyeol').find('.numPer').text(migyeol_per);
               $('.bar._unpsec_r').find('.numPer').text(comp_unpsec_r_per);
               $('.bar._unpsec_a').find('.numPer').text(comp_unpsec_a_per);
               $('.bar._unpsec_e').find('.numPer').text(comp_unpsec_e_per);
               $('.bar._unpsec_c').find('.numPer').text(comp_unpsec_c_per);
               $('.bar._unpsec_r').find('.numCnt').text(com_unpsec_r);
               $('.bar._unpsec_a').find('.numCnt').text(com_unpsec_a);
               $('.bar._unpsec_e').find('.numCnt').text(com_unpsec_e);
               $('.bar._unpsec_c').find('.numCnt').text(com_unpsec_c);
               var getMglRprtInfLst = "<ul class='ulLftlst' onclick='getMigyeolInfo()'>";
                   getMglRprtInfLst += "<li class='w150px'>"+response.sti_nm+"<input type='hidden' name='sti_cd' value='"+response.sti_cd+"' /></li>";
                   getMglRprtInfLst += "<li class='w100px tAlgnCntr fnt500'>"+response.tot_cnt+"</li>";
                   getMglRprtInfLst += "<li class='w100px'>"+response.comp_cnt+" ("+response.comp_per+"%)</li>";
                   getMglRprtInfLst += "<li class='w100px'>"+response.migyeol_cnt+" ("+response.migyeol_per+"%)</li>";
                   getMglRprtInfLst += "<li class='w100px tAlgnRght clrBg_u_r'>"+response.com_unpsec_r+" ("+response.com_unpsec_r_per+"%)</li>";
                   getMglRprtInfLst += "<li class='w100px tAlgnRght clrBg_u_a'>"+response.com_unpsec_a+" ("+response.com_unpsec_a_per+"%)</li>";
                   getMglRprtInfLst += "<li class='w100px tAlgnRght clrBg_u_e'>"+response.com_unpsec_e+" ("+response.com_unpsec_e_per+"%)</li>";
                   getMglRprtInfLst += "<li class='w100px tAlgnRght clrBg_u_c'>"+response.com_unpsec_c+" ("+response.com_unpsec_c_per+"%)</li>";
                   getMglRprtInfLst += "</ul>";
                $('#getMigyeolRprtInfLst').append(getMglRprtInfLst);
               migyeolPerBar();
               ulLftlst();
            console.log(response);
            });

       },
       error: function (request, status, error){
            console.log("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
            alert('데이터를 불러올수 없습니다.');
       }
   });


}

function getAttachFileList() {//미결 파일대리점 반품
     $.ajax({
        url: "/v1/api/tmserp/getAttachFileList",
        type: "GET",
        cache: false,
        dataType: "json",
         data: {
                 attach_file_id:'I20211002010301'
        },
        success: function(list){
            $.each(list, function(idx, response) {
                  var getFileList = "<li><a href='' class='bfafNn icnDown'>"+response.file_nm+"</a> </li>";
                   $('#getAttachFileList').append(getFileList);
               console.log(response);
              });
                 cmma();//콤마
                 innmbr();//인풋값 스팬으로 넘기기
        },
         //complete:function{

         //},
        error: function (request, status, error){
              console.log("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
              alert('데이터를 불러올수 없습니다.');
        }
      });
}
function getVndBanpum() {//대리점 반품
var start = $('.vndBanpumStatus #nmldate1').datepicker('getDate');
var end   = $('.vndBanpumStatus #nmldate2').datepicker('getDate');
if(!start || !end)
    return;
var days = (end - start)/1000/60/60/24;
 if(days >= 30){
	 $('.alrtPop').addClass('opn');
	 $('#vndBSs_popFail').addClass('on');
 }else{
	 $.ajax({
 	    url: "/v1/api/tmserp/getVndBanpum",
 	    type: "GET",
 	    cache: false,
 	    dataType: "json",
          data: {
          	   from_dt: '20210910',
              to_dt:'20211020'
 	    },
 	    success: function(list){
 		    $.each(list, function(idx, response) {

 			    var getVndBanpum = "<ul class='ulLftlst'>";
 			        getVndBanpum += "<li class='w110px'>"+response.plm_cdt+"</li>";
 			        getVndBanpum += "<li class='w150px tAlgnCntr'>"+response.sti_nm+"</li>";
 			        getVndBanpum += "<li class='w100px'>"+response.com_brand_nm+"</li>";
 			        getVndBanpum += "<li class='w150px'>"+response.vnd_nm+"</li>";
 			        getVndBanpum += "<li class='w200px'>"+response.orm_nm+"</li>";
 			        getVndBanpum += "<li class='w150px'>"+response.orm_no+"</li>";
 			        getVndBanpum += "<li class='w100px'>"+response.set_cd+"</li>";
 			        getVndBanpum += "<li class='w100px'>"+response.col_scd+"</li>";
 			        getVndBanpum += "<li class='w130px'>"+response.itm_cd+"</li>";
 			        getVndBanpum += "<li class='w100px'>"+response.col_cd+"</li>";
 			        getVndBanpum += "<li class='w100px tAlgnCntr'><span class='numTxt'></span><input type='text' name='' value="+response.ord_qty+" class='innmbr nmCmma w100p'></li>";
 			        getVndBanpum += "<li class='w100px tAlgnCntr'><span class='numTxt'></span><input type='text' name='' value="+response.wtp_planqty+" class='innmbr nmCmma w100p'></li>";
 			        getVndBanpum += "<li class='w130px tAlgnRght'>"+response.wtp_finish_nm+"</li>";
 			        getVndBanpum += "<li class='w200px'>"+response.wtp_enddt+"</li>";
 			        getVndBanpum += "<li class='w100px tAlgnCntr'><button class='vndBSsBtn'>"+response.com_rdsec_nm+"</button></li>";
 			        getVndBanpum += "<li class='w130px tAlgnRght'>"+response.com_undsec_nm+"</li>";
 			        getVndBanpum += "</ul>";
 	  		     $('#getVndBanpumLst').append(getVndBanpum);
 			 console.log(response);
  	          });
                  cmma();//콤마
                  innmbr();//인풋값 스팬으로 넘기기
                  vndBSsFilePop();//첨부파일
                  /*$('.vndBSsBtn').each(function() {
                  var vndBSsBtntxt = $(this).text();
                     if(vndBSsBtntxt == "완결"){
                          $(this).addClass('non');
                     }else{
                          $(this).removeClass('non');
                     }
                });*/
 	    },
          //complete:function{

          //},
 	    error: function (request, status, error){
               console.log("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
               alert('데이터를 불러올수 없습니다.');
 	    }
 	  });
    }


}


function cnstrctLst() {//시공건 검색 리스트
var from_dt = $('.cnstrctLstBx #nmldate1').val();//시작일
var to_dt = $('.cnstrctLstBx #nmldate2').val();//종료일
var orm_nm = $('input[name=orm_nm]').val();//건명
var itm_cd = $('input[name=itm_cd]').val();//단품코드
var start = $('.cnstrctLstBx #nmldate1').datepicker('getDate');
var end   = $('.cnstrctLstBx #nmldate2').datepicker('getDate');
if(!start || !end)
    return;
var days = (end - start)/1000/60/60/24;
 if((orm_nm.length < 1 && itm_cd.length < 1) || days >= 30){
	 $('.alrtPop').addClass('opn');
	 $('#resList_popFail').addClass('on');
 }else{
	 $.ajax({
 	    url: "/v1/api/tmserp/getResmstList",
 	    type: "GET",
 	    cache: false,
 	    dataType: "json",
          data: {
          	   from_dt: '20210910',
              to_dt:'20211020',
              com_scd: 'C16YA',
              ksti_cd: 'YA521',
              orm_nm: '(임)윤미주',
              itm_cd: 'DASDQEW2'
 	    },
 	    success: function(list){
 		    $.each(list, function(idx, response) {
 	                   var rem_dt = response.rem_dt;
 			         var com_ssec = response.com_ssec;
 	                   var com_brand = response.com_brand;
 	                   var orm_no = response.orm_no;
 	                   var orm_nm = response.orm_nm;
 	                   var orm_amt = response.orm_amt;
 	                   var orm_addr = response.orm_addr;
 	                   var sti_nm = response.sti_nm;

 			    var cnstrctLst = "<ul class='ulLftlst' onclick='cnstrctLst_dtlInf()'>";
 			        cnstrctLst += "<li class='w110px'>"+response.rem_dt+"</li>";
                        cnstrctLst += "<li class='w100px'>"+response.com_ssec+"</li>";
                        cnstrctLst += "<li class='w100px'>"+response.com_brand+"</li>";
                        cnstrctLst += "<li class='w150px'>"+response.orm_no+"</li>";
                        cnstrctLst += "<li class='w300px'>"+response.orm_nm+"</li>";
                        cnstrctLst += "<li class='numTxt w150px'><input type='text' name='' value='"+response.orm_amt+"' class='innmbr nmCmma'></li>";
                        cnstrctLst += "<li class='w500px'>"+response.orm_addr+"</li>";
                        cnstrctLst += "<li class='w150px'>"+response.sti_nm+"</li>";
                        cnstrctLst += "</ul>";
 	  		     $('#cnstrctLst').append(cnstrctLst);
 			 console.log(response);
  	          });
                  cmma();//콤마
                  innmbr();//인풋값 스팬으로 넘기기
 	    },
          //complete:function{

          //},
 	    error: function (request, status, error){
               console.log("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
               alert('데이터를 불러올수 없습니다.');
 	    }
 	  });
    }


}
function cnstrctLst_dtlInf() {//상세정보
     $('#cnstrctLst_dtlInf').find('.ulLftlst._data').remove();
	$.ajax({
	    url: "/v1/api/tmserp/getResdtlList",
	    type: "GET",
	    cache: false,
	    dataType: "json",
         data: {
         	   orm_no: 'I20211001000100'
	    },
	    success: function(list){
		    $.each(list, function(idx, response) {
	                   var itm_cd = response.itm_cd;
			         var col_cd = response.col_cd;
	                   var ord_qty = response.ord_qty;
	                   var itm_nm = response.itm_nm;
	                   var orm_amt = response.orm_amt;

			    var cnstrctLst_dtlInf = "<ul class='ulLftlst _data'>";
			        cnstrctLst_dtlInf += "<li class='w150px'>"+itm_cd+"</li>";
                       cnstrctLst_dtlInf += "<li class='w150px tAlgnCntr'>"+col_cd+"</li>";
                       cnstrctLst_dtlInf += "<li class='w100px tAlgnCntr'><span class='numTxt'></span><input type='text' name='' value='"+ord_qty+"' class='innmbr nmCmma w100p'></li>";
                       cnstrctLst_dtlInf += "<li class='wCal550px'>"+itm_nm+"</li>";
                       cnstrctLst_dtlInf += "<li class='w150px tAlgnRght'><span class='numTxt'></span><input type='text' name='' value='"+orm_amt+"' class='innmbr nmCmma w100p'></li>";
                       cnstrctLst_dtlInf += "</ul>";
	  		     $('#cnstrctLst_dtlInf').append(cnstrctLst_dtlInf);
			 console.log(response);
 	          });
                 cmma();//콤마
                 innmbr();//인풋값 스팬으로 넘기기
	    },
         //complete:function{

         //},
	    error: function (request, status, error){
              console.log("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
              alert('데이터를 불러올수 없습니다.');
	    }
	  });

}
