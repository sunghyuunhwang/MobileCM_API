//모바일CM 프론트엔드 이벤트
//작업자 이커머스팀 : 심민아
//백엔드에 필요한 이벤트는 함수 혹은 함수안의 이벤트를 사용해 주시고
//백엔드에 사용한 프론트엔드이벤트가 $(document).ready(function()에서 필요 없을경우 주석 처리해주세요.*주석 처리시설명필수표시
function cnstrctLstPop() {//시공건 팝업
     $('.cnstrctLstBtn').click(function() {
       $('.cnstrctLstPop').addClass('opn');
       datepicker();
     });
}
function ulLftlst() {//리스트표시
     $('.ulLftlst').each(function() {
      var ulLftlst = $(this);
      $(this).click(function() {
           if(! $(this).hasClass('_index')){
           $('.ulLftlst').removeClass('on');
           $(this).addClass('on');
           resetUlLftdtllst();
           cnstrctLst_dtlInf(this);
           }
      });
    });
}
function resetUlLftlst() {
    $('#cnstrctLst .ulLftlst').each(function() {
    	if(! $(this).hasClass('_index')){
 				$(this).remove();
           }
      });
}
function resetUlLftdtllst() {
     $('#cnstrctLst_dtlInf .ulLftlst').each(function() {
     	if(! $(this).hasClass('_index')){
 				$(this).remove();
           }
      	});
	
}
function datepicker() {//달력 한글화
   $( ".datepicker" ).datepicker({
      changeMonth: true,
      //dateFormat:"yymmdd",
      dateFormat:"m월    dd일",
      showMonthAfterYear: true,
	  yearSuffix: '년',
      onSelect: function (dateText, inst) {
		$('.apiDtPckr').datepicker("setDate",  $('.datepicker').datepicker('getDate'));
		//$(".apiDtPckr").val($.datepicker.formatDate("yymmdd", dateText));
       	allRset();//리셋(프론트엔드부분만)
        assgnCll();//다시 불러오기
      }, 
  }).datepicker("setDate", new Date());
  $( ".apiDtPckr" ).datepicker({
   changeMonth: true,
   dateFormat:"yymmdd",
   showMonthAfterYear: true,
   yearSuffix: '년',
 }).datepicker("setDate", new Date());
 $( ".nmldatepicker" ).datepicker({
 changeMonth: true,
 prevText:"이전달",
 nextText:"다음달",
 dateFormat:"yy-mm-dd",
 showMonthAfterYear: true,
 monthNames: ['1', '2', '3', '4', '5', '6', '7', '8', '9', '10', '11', '12'],
 monthNamesShort: ['1', '2', '3', '4', '5', '6', '7', '8', '9', '10', '11', '12'], 
 dayNames: ['일', '월', '화', '수', '목', '금', '토'],
 dayNamesShort: ['일', '월', '화', '수', '목', '금', '토'],
 dayNamesMin: ['일', '월', '화', '수', '목', '금', '토'],
 yearSuffix: '년'
}).datepicker("setDate", new Date());
if($('.innmldate').hasClass('opn')){
     $('.ui-datepicker').addClass('nml');
}else{
     $('.ui-datepicker').removeClass('nml');
}

}
function cmma() { // 세자리수 콤마찍기 - 읽기 전용
  $('.nmCmma').each(function() {
    var employ = $(this).val();
    var cmmaVal = "";
    employ = employ.replace(/,/g, '');
    for (var i = 1; i <= employ.length; i++) {
      if (i > 1 && (i % 3) == 1) {
        cmmaVal = employ.charAt(employ.length - i) + "," + cmmaVal;
      } else {
        cmmaVal = employ.charAt(employ.length - i) + cmmaVal;
      }
    }
    $(this).val(cmmaVal);
  });
}

function hipun() { // 세자리수 콤마찍기 - 읽기 전용
  $('.hipun').each(function() {
    var employ = $(this).val();
    var formatNum = "";
    employ = employ.replace(/,/g, '');
    if(employ.length == 8) {
		formatNum = employ.replace(/(\d{4})(\d{2})(\d{2})/, '$1-$2-$3');
		}
    $(this).val(formatNum);
  });
}

function innmbr() { // 인풋 넓이 조절을 위한 금액을 span에 보이기
  $('.innmbr').each(function() {
    var innmbr = $(this).val();
    $(this).prev('.numTxt').text(innmbr);
  });
}
function inTxt() {//인풋텍스트 박스에 넣기
     $('.inptTxt').each(function() {
       var inptTxt = $(this).val();
       $(this).prev('.inTxt').text(inptTxt);
     });
}

function assgnLstCkck() { // 할당리스트 클릭시
var menuBox = $('.fssMblCm._Map .menuBox');
var assgnSubBox = $('.assgnSubBox.onoffBx');
    var assgnli = $('.assgnLst li');
    var assgnliclck = assgnli.find('.assgnBx .fao > .txt');
    assgnli.click(function() {
       var li = $(this);
        if(li.parent().hasClass('inChck')){
            li.toggleClass('clck');
       }
   });
   assgnliclck.click(function() {
        $('.crld').remove();
        $('#chsAssgnInfBx').removeClass('opn');
        $('.chngTmBx1').removeClass('chng');
        $('.chngTmLstBx').removeClass('chng');
        $('.chngTmBtn').removeClass('chng');
        $('.assgnInfLstPop').removeClass('opn');//품목리스트 숨기기
       var li = $(this).parent().parent().parent().parent();
       var on = $(this).parent().parent().parent();
       var assgn = li.find('li');
  //      if(li.hasClass('assgnDn')){
          //$(this).removeClass('pEvntNon');
          li.find('li').removeClass('clck');
          on.toggleClass('clck');
          if(li.find('chck')){
            menuBox.addClass('on');
            assgnSubBox.addClass('on');
          }
          var crclClr = $('li.clck .crlclOn').css('background-color');
               $('.assgnTm .crlclOn').css('background-color',crclClr);

//        }else{
            // $(this).addClass('pEvntNon');
//        }
  });
    $('.inChck li').click(function() {
            var inchck = $(this);
            var chck = inchck.find('.chck');
              if(inchck.hasClass('clck')){
                   chck.prop('checked', true);
              }
    });

}
function assgnsubLstChk() {//선택팀 리스트
     $('.assgnSubLst > li').click(function() {
         $('.assgnSubLst > li').removeClass('on');
          var clck = $(this);
         var crclClr = $('#assgn_lst li.clck .crlclOn').css('background-color');
          clck.toggleClass('on');
          if(clck.hasClass('on')){
               clck.find('.crlclOn').css({'background-color':crclClr,'transition':'0.3s'});
          }
     });

}
function onoffBxCls() { // 박스 열고닫기
  $('.onoffBx .clsBtn').each(function() {
       var cls = $(this);
       var onoffBx = cls.parents('.onoffBx');
       var menuBox = $('.fssMblCm._Map .menuBox');
       cls.click(function() {
            onoffBx.removeClass('on');
            if(menuBox.hasClass('on')){
                 menuBox.removeClass('on');
            }
       });
  });
}
function inNrrwWid() {//리스트 스타일 바꾸기
  $('.inNrrwWid').each(function() {
     var nrrwBtn = $(this).find('.nrrwBtn');
     var widBtn = $(this).find('.widBtn');
     var nrrwWid = $(this).find('.nrrwwidBx .nrrwwid');
     nrrwBtn.click(function() {
          nrrwBtn.removeClass('off');
          nrrwBtn.addClass('on');
          widBtn.removeClass('on');
          widBtn.addClass('off');
          nrrwWid.removeClass('_wid');
          nrrwWid.addClass('_nrrw');
     });
     widBtn.click(function() {
          widBtn.removeClass('off');
          widBtn.addClass('on');
          nrrwBtn.removeClass('on');
          nrrwBtn.addClass('off');
          nrrwWid.removeClass('_nrrw');
          nrrwWid.addClass('_wid');
     });

  });
}
function popCls() {//각 팝업 닫기 공통
  $('.fssMblCmPop').each(function() {
     var pop = $(this);
     var cls = pop.find('.clsBtn');
     var cntnst = pop.find('.popCntnts');
     cls.click(function() {
          pop.removeClass('opn');
          cntnst.removeClass('on');
     });
  });
}

function chckLnth() {//체크갯수 세기
     $('#assgnPop_lst li').click(function() {
          var chck = $(this);
          var lngthLi = $('#assgnPop_lst .clck');
          var lngth = $('.assgnPlChckLngth');
          lngth.val(lngthLi.length);
          innmbr();
     });
}
function allChck() {//전체체크
  $('.allChckBx').each(function() {
     var bx = $(this);
     var allChck = bx.find('.allChck');
     var chck = bx.find('.chck');
     var inchckbx = bx.find('.inChck');
     var inchck = inchckbx.find('li');
     allChck.click(function() {
          if(allChck.is( ':checked' )){
               chck.prop('checked', true);
               if(inchck.length > 0){
                         inchck.addClass('clck');
               }
          }else{
               chck.prop('checked', false);
               if(inchck.length > 0){
                    inchck.removeClass('clck');
               }
          }
          var lngthLi = $('#assgnPop_lst .clck');
          var lngth = $('.assgnPlChckLngth');
          lngth.val(lngthLi.length);
          innmbr();
     });
     chck.click(function() {
          if(chck.is( ':checked' ).length == chck.length){
               allChck.prop('checked', true);
          }else{
               allChck.prop('checked', false);
          }
     });
     inchck.click(function() {
          if(inchck.length == $('.allChckBx').find('.clck').length){
               allChck.prop('checked', true);
          }else{
               allChck.prop('checked', false);
          }
     });
  });
}

function issonoff() {//이슈 표시
     $('.chkIcnLst.iss li').each(function() {
          var iss = $(this);
          var issrsrt = $(this).text();
          if(issrsrt == 'Y'){
                 iss.addClass('on');
          }else if(issrsrt == 'N'){
                 iss.removeClass('on');
          }
     });

}
function assgnsubInf() {//선택팀 상세
     var vInfTmNm = $('#chsAssgnInfBx .assgnTm');
     var vInfclr = vInfTmNm.find('.crlclOn');
     var vInfNm = $('#chsAssgnInfBx .faoCtmrNm');
     var vInfiss = $('#chsAssgnInfBx .chkIcnLst.iss');
     var vassgnSub_lst =$('#assgnSub_lst .on .crlclOn');
     $('.assgnSbInfBx').click(function() {
         $('.crld').remove();
          var ths = $(this);
          var latitudeNbr = ths.find('.latitudeNbr');
          var longitudeNbr = ths.find('.longitudeNbr');
          var cinf = ths.find('.fao .txt');
          var cinfTxt = cinf.text();
          var ciss = ths.find('.chkIcnLst.iss');
          var tmNm = $('.assgnLst .clck > .assgnBx .fao .txt');
          var crclClr = $('.assgnLst .clck .crlclOn').css('background-color');
          vInfTmNm.find('.fao').empty();
          vInfTmNm.find('.fao').text(tmNm.text());
          vInfNm.text(cinf.text());
          vInfiss.find('li').remove();
          vInfiss.prepend(ciss.html());
          vInfclr.css({'background-color':crclClr,'transition':'0.3s'});
          //vassgnSub_lst.css({'background-color':crclClr,'transition':'0.3s'});
          $('#chsAssgnInfBx').addClass('opn');
          $('#assgnInfLstPop').addClass('opn');
//          assgninfLstdt(this);
                    var strLat= latitudeNbr.val();
                    var strLng= longitudeNbr.val();
                       lonlat =  new Tmapv2.LatLng(strLat,strLng);
                       //map.setCenter(lonlat);
                       var idxNbr = $('#assgn_lst li').index($('#assgn_lst li[class="clck"]'));
                       var content =	"<div class='crld crclLnds"+idxNbr+"'><span class='mrkLvlTxt'>"+cinfTxt+"</span></div>"; // 라벨 내용 지정
                       var popup = new Tmapv2.InfoWindow({
                            position : new Tmapv2.LatLng(strLat,strLng), //Popup 이 표출될 맵 좌표
                            content : content, //Popup 크기는 스타일로 지정, Popup 표시될 text
                            type : 2, // 팝업 타입
                            map: map // 지도 객체
                     });
//                     map.setZoom(16);
                     $('.crld').parent().css({'margin':'-35px 0 0 -22px','border':'0','background':'none'});
     });
}
//function mrkLvlClck(param) {//마커 클릭시 해달 리스트 표시
//    var pos = param.getPosition();
//    var clat = pos.lat();
//    var clng =  Math.round(pos.lng()*1000000)/1000000;
//	$('#assgnSub_lst > li').each(function(idx) {
//		var lat = $(this).find('input[class=latitudeNbr]').val();
//		var lng = $(this).find('input[class=longitudeNbr]').val();
//		alert( "idx : " + idx +" clat : " + clat + " lat " + lat + " clng : " + clng + " lng " + lng + " lat? " + (clat == lat) + " lng? "  + (clng == lng));
//		if(clat == lat && clng == lng) {
//			$('input:checkbox[id="chks_'+idx+'"]').prop("checked", true);
//			return;
//		}
//	});
//}
function assgnsubInfOnly() {//선택팀 상세팝업팀변경 액션
	$('.chngTmBx1').addClass('chng');
	$('.chngTmLstBx').addClass('chng');
	$('.chngTmBtn').addClass('chng');
     $('#chngTmLst li').remove();
     $('#assgnInfLstPop').removeClass('opn');

}
function chngTmLst() {//팀변경리스트 클릭
     $('.chngTmLst li').click(function() {
          var clck = $(this);
          $('.chngTmLst li').removeClass('on');
          clck.toggleClass('on');
          $('.chngTm').text(clck.text());
          $('.chngTm').addClass('don');
     });
}
function chngTmCls() {//팀변경 취소
     $('.chngTmCls').click(function() {
        $('.chngTmBx1').removeClass('chng');
        $('.chngTmLstBx').removeClass('chng');
        $('.chngTm').removeClass('don');
        $('.chngTm').text('미지정');
        $('.chngTmBtn').removeClass('chng');
        $('#assgnInfLstPop').addClass('opn');
        $('#assgnInfLstPop').addClass('smll');
     });
}
function chngTmDonPop() {//팀 변경 완료팝업
     $('.chngTmBtn').click(function() {
	   var cnt = $('input:checkbox[name="fromsti2"]:checked').length;
	   if (cnt < 1) {
		alert("변경할 수주건을 체크해주세요.");
		return;
	   };

	   var isSelected = $('.chngTm').hasClass('don');
	   if(!isSelected) {
		alert("변경할 팀을 선택해주세요.");
		return;
	   };
	   var check = true;
	   $('input:checkbox[name="fromsti2"]:checked').each(function () {
			var com_rfg = $(this).closest('li').find('input[name=comrfg]').val();
			var com_ssec = $(this).closest('li').find('input[name=from_com_ssec]').val();
			if(com_rfg === "예약확정") {
			   check = false;
			}
	   	});
	   if(!check) {
	       $('.alrtPop').addClass('opn');
	       $('#chngTmDonPopFail').addClass('on');
	       return;
	   }
       $('.alrtPop').addClass('opn');
       $('#chngTmDonPop').addClass('on');
     });
}
function widnrrwActn() {//리스트 넓고 좁게보기
     var widBtn = $('.assgnSubBox .icnWidBx');
     var nrrwBtn = $('.assgnSubBox .icnNrrwBx');
     var menuBox = $('.fssMblCm._Map .menuBox');

     widBtn.click(function(event) {
          widBtn.removeClass('on');
          nrrwBtn.addClass('on');
          menuBox.addClass('wid');
          $('.assgnSubBox .clsBtn').hide();
          $('.menuSldBtn').hide();
     });
     nrrwBtn.click(function(event) {
          nrrwBtn.removeClass('on');
          widBtn.addClass('on');
          menuBox.removeClass('wid');
          $('.assgnSubBox .clsBtn').show();
          $('.menuSldBtn').show();
     });
}
function swipeActn() {//터치액션
      var assgnInfLst = $('#assgnInfLstPop');//시공상세리스트(오른쪽하단)
       var assgnInfLstBox = assgnInfLst.find('.lstScllBx');//시공상세리스트박스
      var assgnInfLstBtn = $('#assgnInfLstPop .popSldBTn');//시공상세리스트(오른쪽하단)
        assgnInfLstBtn.swipe({
            swipe:function(event, direction, distance, duration, fingerCount, fingerData) {
                if( direction == "up" ){
                    if(assgnInfLst.hasClass('smll')){
                          assgnInfLst.removeClass('smll');
                          assgnInfLstBox.removeClass('smll mddl max');
                    }else if(assgnInfLst.hasClass('hrf')){
                          assgnInfLst.removeClass('smll');
                          assgnInfLst.removeClass('hrf');
                          assgnInfLst.addClass('full');
                          assgnInfLstBox.removeClass('smll mddl max');
                          assgnInfLstBox.addClass('max');
                    }else{
                          assgnInfLst.removeClass('smll');
                         assgnInfLst.removeClass('full');
                         assgnInfLst.addClass('hrf');
                         assgnInfLstBox.removeClass('smll mddl max');
                         assgnInfLstBox.addClass('mddl');
                    }
               }else if( direction == "down" ){
                    if(assgnInfLst.hasClass('full')){
                          assgnInfLst.removeClass('full');
                          assgnInfLst.addClass('hrf');
                          assgnInfLstBox.removeClass('smll mddl max');
                          assgnInfLstBox.addClass('mddl');
                    }else if(assgnInfLst.hasClass('hrf')){
                         assgnInfLst.removeClass('hrf');
                         assgnInfLstBox.removeClass('smll mddl max');
                    }else{
                       assgnInfLst.addClass('smll');
                       assgnInfLstBox.removeClass('smll mddl max');
                    }
            }
        },
       threshold:0
    });

    var menuSldBtn = $('.menuSldBtn');//메뉴 슬라이드버튼
    var menu = $('.menuBox');//메뉴 슬라이드버튼
    var menusub = $('.assgnSubBox');
    menuSldBtn.swipe({
       swipe:function(event, direction, distance, duration, fingerCount, fingerData) {
            if( direction == "left" ){
                if(menu.hasClass('on')){
                      menu.addClass('nrrw');
                      menusub.removeClass('on');
                }else{
                }
           }else if( direction == "right" ){
                if(menu.hasClass('nrrw')){
                      menu.removeClass('nrrw');
                      menusub.addClass('on');
                }else{
                }
           }
        },
        threshold:0
     });
}
function assgnDonPop() {//분배확정팝업
     $('.assgnDon').click(function() {
          $('.alrtPop').addClass('opn');
          $('#assgnDonPop').addClass('on');

     });
}
function rsvtnStrtPop() {//예약확정 팝업
     $('.rsvtnStrt').click(function() {
          $('.alrtPop').addClass('opn');
          $('#rsvtnStrtPop').addClass('on');
     });
}
function logoutPop() {//로그아웃 팝업
     $('.icnLogout').click(function() {
          $('.alrtPop').addClass('opn');
          $('#logoutPop').addClass('on');
     });
}
function allRset() {//모두 처음으로 돌리기(프론트엔드부분만)
     $('.scdlLstBox').removeClass('on');//처음 할당리스트(분배하기리스트) 숨기기(왼쪽메뉴)
     $('#assgn_lst li').remove();//분배받은 리스트 모두 지우기
     $('.mapCll').addClass('on');//지도좌표불러오기 버튼 보이기
     $('.menuBox .assgnStrt').removeClass('on');//스케줄분배하기버튼 숨기기
     $('.rsvtnStrt').removeClass('on');//예약확정 버튼 숨기기
     $('.chsAssgnInfBx').removeClass('opn');//상세선택팀(개뱔팀변경)박스 숨기기
     $('.assgnInfLstPop').removeClass('opn');//품목리스트 숨기기
     if($('.menuBox').hasClass('on')){//메뉴박스 상세리스트(선택팀) 열려있으면
        $('.menuBox').removeClass('on');//닫고
         $('.assgnSubBox ').removeClass('on');//상세리스트(선택팀)도 닫고
         $('.assgnSub_lst li').remove();//상세리스트(선택팀)지우기
     }
     //스케줄 분배하기도 원래모습으로
     $('.menuBox .assgnStrt').removeClass('btnBlckLn');
     $('.menuBox .assgnStrt').removeClass('btnGry');
     $('.menuBox .assgnStrt').addClass('btnBlck');
     $('.crld').remove();
     $('.chngTm').removeClass('don');
     $('.chngTm').text('미지정');
}
$(document).ready(function(){
      if($('.loginBx').length < 1){
           $(".datepicker").val($.datepicker.formatDate("m월    dd일", new Date()));
           $(".apiDtPckr").val($.datepicker.formatDate("yymmdd", new Date()));
            datepicker();//달력 한글화
            swipeActn();//터치액션
      }
      cmma(); // 세자리수 콤마찍기 - 읽기 전용
      innmbr(); // 인풋 넓이 조절을 위한 금액을 span에 보이기
      assgnLstCkck();// 할당리스트 클릭시
      onoffBxCls(); // 박스 열고닫기
      inNrrwWid();//리스트 스타일 바꾸기
      popCls();//각 팝업 닫기 공통
      allChck();//전체체크
      assgnsubLstChk();//선택팀 리스트
     // assgnsubInf();//선택팀 상세
      chngTmDonPop();//팀 변경 완료팝업
      chngTmCls();//팀변경 취소
      widnrrwActn();//리스트 넓고 좁게보기
      assgnDonPop();//분배확정팝업
      rsvtnStrtPop();//예약확정 팝업
      logoutPop();//로그아웃 팝업
      ulLftlst();//리스트표시
      cnstrctLstPop();//시공건 팝업
});
