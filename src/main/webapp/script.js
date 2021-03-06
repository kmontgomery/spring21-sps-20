// Copyright 2020 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

/** Fetches tasks from the server and adds them to the DOM */
function loadResources() {
    fetch('/list-news').then(response => response.json()).then((resources) => {
      const resourceListElement = document.getElementById('very-very-unique-news');
      resources.forEach((resource) => {
        resourceListElement.appendChild(createResourceElement(resource));
      })
    });
}

function createResourceElement(resource) {
  const resourceElement = document.createElement('li');
  resourceElement.className = 'tool';

  const titleElement = document.createElement('h6');
  titleElement.innerText = resource.newsTitle;

  const placeElement = document.createElement('h6');
  placeElement.innerText = resource.newsPlace;

  const linkElement = document.createElement('h6');
  linkElement.innerText = resource.newsLink;

  resourceElement.append(titleElement);
  resourceElement.append(placeElement);
  resourceElement.append(linkElement);
  return resourceElement;
}

window.smoothScroll = function(target) {
  let scrollContainer = target;
  do { 
      scrollContainer = scrollContainer.parentNode;
      if (!scrollContainer) return;
      scrollContainer.scrollTop += 1;
  } while (scrollContainer.scrollTop == 0);

  let targetY = 0;
  do { 
      if (target == scrollContainer) break;
      targetY += target.offsetTop;
  } while (target = target.offsetParent);

  scroll = function(c, a, b, i) {
      i++; if (i > 30) return;
      c.scrollTop = a + (b - a) / 30 * i;
      setTimeout(function(){ scroll(c, a, b, i); }, 20);
  }
  
  scroll(scrollContainer, scrollContainer.scrollTop, targetY, 0);
}

window.smoothScrollOtherPage = function(targetContainer, targetURLstring) {
    let thisURL = new URL('http://spring21-sps-20.appspot.com');
    let targetURL = new URL(targetURLstring, thisURL);
    window.location.href = `${targetURL}#${targetContainer}`;
}